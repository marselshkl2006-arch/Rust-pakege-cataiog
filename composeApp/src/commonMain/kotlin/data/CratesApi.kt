package data

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.URLBuilder
import io.ktor.http.appendPathSegments
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import model.Crate
import model.CrateDetail
import model.CrateVersion
import model.Owner
import kotlin.coroutines.cancellation.CancellationException

const val DEFAULT_PER_PAGE = 20

const val MAX_PER_PAGE = 100

sealed class ApiError(message: String, cause: Throwable? = null) : Exception(message, cause) {
    class Network(cause: Throwable?) :
        ApiError("Network error" + (cause?.message?.let { ": $it" } ?: ""), cause)

    class Timeout(cause: Throwable?) : ApiError("Request timed out", cause)
    class NotFound(val what: String) : ApiError("Not found: $what")
    class RateLimited(val retryAfterSeconds: Int?) :
        ApiError("Too many requests (HTTP 429)" + (retryAfterSeconds?.let { ", retry after ${it}s" } ?: ""))

    class Http(val code: Int) : ApiError("Server returned HTTP $code")
    class Parse(cause: Throwable?) : ApiError("Unexpected response format", cause)
}

fun Throwable.toUserMessage(): String = message ?: "Unknown error"

/** What the repository needs from a data source: the real API or the hand-written mocks. */
interface CratesDataSource {
    suspend fun search(query: String, page: Int, perPage: Int = DEFAULT_PER_PAGE): List<Crate>
    suspend fun getCrate(name: String): CrateDetail
    suspend fun getDependencies(name: String): List<String>
    suspend fun getOwners(name: String): List<Owner>
    suspend fun getUser(login: String): Owner
}

internal val CratesJson = Json {
    ignoreUnknownKeys = true
    coerceInputValues = true
}

/** Builds the Ktor client. Every request carries the mandatory User-Agent header. */
fun createCratesClient(engine: HttpClientEngine? = null): HttpClient {
    val configure: HttpClientConfig<*>.() -> Unit = {
        install(UserAgent) { agent = CratesApi.USER_AGENT }
        install(HttpTimeout) { requestTimeoutMillis = 15_000 }
        install(ContentNegotiation) { json(CratesJson) }
    }
    return if (engine != null) HttpClient(engine, configure) else HttpClient(configure)
}

class CratesApi(
    private val client: HttpClient,
    private val cache: Cache = Cache(),
    private val baseUrl: String = BASE_URL,
    private val userRoot: String = USERS_URL,
) : CratesDataSource {

    @Serializable
    private class SearchResponse(val crates: List<Crate>)

    @Serializable
    private class CrateResponse(val crate: Crate, val versions: List<CrateVersion> = emptyList())

    @Serializable
    private class OwnersResponse(val users: List<Owner>)

    @Serializable
    private class UserResponse(val user: Owner)

    @Serializable
    private class DependenciesResponse(val dependencies: List<Dependency>)

    @Serializable
    private class Dependency(@SerialName("crate_id") val crateId: String)

    override suspend fun search(query: String, page: Int, perPage: Int): List<Crate> {
        val url = URLBuilder(baseUrl).apply {
            if (query.isNotBlank()) parameters.append("q", query.trim()) else parameters.append("sort", "downloads")
            parameters.append("page", page.coerceAtLeast(1).toString())
            parameters.append("per_page", perPage.coerceIn(1, MAX_PER_PAGE).toString())
        }.buildString()
        return decode<SearchResponse>(fetchText(url, "search")).crates
    }

    override suspend fun getCrate(name: String): CrateDetail {
        val response = fetchCrate(name)
        val owners = getOwners(name)
        val dependencies = getDependencies(name)
        return CrateDetail(
            crate = response.crate.copy(dependencies = dependencies, owners = owners.map { it.login }),
            versions = response.versions,
            owners = owners,
            dependencies = dependencies,
        )
    }

    override suspend fun getDependencies(name: String): List<String> {
        val version = fetchCrate(name).crate.version
        val url = URLBuilder(baseUrl).apply { appendPathSegments(name, version, "dependencies") }.buildString()
        return decode<DependenciesResponse>(fetchText(url, "$name dependencies"))
            .dependencies.map { it.crateId }.distinct()
    }

    override suspend fun getOwners(name: String): List<Owner> {
        val url = URLBuilder(baseUrl).apply { appendPathSegments(name, "owners") }.buildString()
        return decode<OwnersResponse>(fetchText(url, "$name owners")).users
    }

    override suspend fun getUser(login: String): Owner {
        val url = URLBuilder(userRoot).apply { appendPathSegments(login) }.buildString()
        return decode<UserResponse>(fetchText(url, login)).user
    }

    fun close() = client.close()

    private suspend fun fetchCrate(name: String): CrateResponse {
        val url = URLBuilder(baseUrl).apply { appendPathSegments(name) }.buildString()
        return decode(fetchText(url, name))
    }

    private suspend fun fetchText(url: String, what: String): String {
        cache.get(url)?.let { return it }
        val text = try {
            val response = client.get(url)
            when (val status = response.status.value) {
                in 200..299 -> Unit
                404 -> throw ApiError.NotFound(what)
                429 -> throw ApiError.RateLimited(response.headers[HttpHeaders.RetryAfter]?.toIntOrNull())
                else -> throw ApiError.Http(status)
            }
            response.bodyAsText()
        } catch (e: CancellationException) {
            throw e
        } catch (e: ApiError) {
            throw e
        } catch (e: Throwable) {
            throw if (isTimeout(e)) ApiError.Timeout(e) else ApiError.Network(e)
        }
        cache.put(url, text)
        return text
    }

    private fun isTimeout(e: Throwable): Boolean =
        e is HttpRequestTimeoutException || e::class.simpleName?.contains("Timeout", ignoreCase = true) == true

    private inline fun <reified T> decode(text: String): T = try {
        CratesJson.decodeFromString<T>(text)
    } catch (e: IllegalArgumentException) { // SerializationException extends IllegalArgumentException
        throw ApiError.Parse(e)
    }

    companion object {
        const val USER_AGENT = "MyCatalog/1.0 (me@example.com)"
        const val API_ROOT = "https://crates.io/api/v1"
        const val BASE_URL = "$API_ROOT/crates"
        const val USERS_URL = "$API_ROOT/users"
    }
}
