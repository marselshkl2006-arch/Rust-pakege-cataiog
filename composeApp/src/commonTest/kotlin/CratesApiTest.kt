import data.ApiError
import data.Cache
import data.CratesApi
import data.createCratesClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandler
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class CratesApiTest {
    private val jsonHeaders = headersOf(HttpHeaders.ContentType, "application/json")

    private fun api(handler: MockRequestHandler) = CratesApi(createCratesClient(MockEngine(handler)), Cache())

    private val searchJson = """
        {"crates":[
          {"id":"serde","name":"serde","max_version":"1.0.228","description":"A framework","downloads":123,
           "repository":"https://github.com/serde-rs/serde","documentation":null,"homepage":null,"unknown_field":1},
          {"id":"tokio","name":"tokio","max_version":"1.47.1","description":null,"downloads":456}
        ],"meta":{"total":2}}
    """.trimIndent()

    @Test
    fun parsesSearchResponse() = runTest {
        val crates = api { respond(searchJson, HttpStatusCode.OK, jsonHeaders) }.search("serde", 1, 20)
        assertEquals(listOf("serde", "tokio"), crates.map { it.name })
        assertEquals("1.0.228", crates[0].version)
        assertEquals(123L, crates[0].downloads)
        assertEquals(null, crates[1].description)
    }

    @Test
    fun sendsUserAgentOnEveryRequest() = runTest {
        val seen = mutableListOf<String?>()
        val api = api { request ->
            seen += request.headers[HttpHeaders.UserAgent]
            respond(searchJson, HttpStatusCode.OK, jsonHeaders)
        }
        api.search("serde", 1, 20)
        api.search("tokio", 2, 20)
        assertEquals(2, seen.size)
        seen.forEach { assertEquals(CratesApi.USER_AGENT, it) }
    }

    @Test
    fun sendsQueryAndPagingParameters() = runTest {
        var query: String? = null
        var page: String? = null
        var perPage: String? = null
        api { request ->
            query = request.url.parameters["q"]
            page = request.url.parameters["page"]
            perPage = request.url.parameters["per_page"]
            respond(searchJson, HttpStatusCode.OK, jsonHeaders)
        }.search("serde", 3, 500)
        assertEquals("serde", query)
        assertEquals("3", page)
        assertEquals("100", perPage) // clamped to the API maximum
    }

    @Test
    fun mapsNotFoundTo404Error() = runTest {
        val api = api { respondError(HttpStatusCode.NotFound) }
        assertFailsWith<ApiError.NotFound> { api.getCrate("nope") }
    }

    @Test
    fun mapsTooManyRequests() = runTest {
        val api = api { respondError(HttpStatusCode.TooManyRequests) }
        assertFailsWith<ApiError.RateLimited> { api.search("serde", 1, 20) }
    }

    @Test
    fun mapsServerErrorsAndGarbage() = runTest {
        assertFailsWith<ApiError.Http> {
            api { respondError(HttpStatusCode.InternalServerError) }.search("x", 1, 20)
        }
        assertFailsWith<ApiError.Parse> {
            api { respond("not json", HttpStatusCode.OK, jsonHeaders) }.search("x", 1, 20)
        }
    }

    @Test
    fun getCrateCombinesCrateOwnersAndDependencies() = runTest {
        val api = api { request ->
            val path = request.url.encodedPath
            val body = when {
                path.endsWith("/owners") ->
                    """{"users":[{"id":1,"login":"dtolnay","name":"David Tolnay","avatar":"https://example.com/a.png"}]}"""

                path.endsWith("/dependencies") ->
                    """{"dependencies":[{"crate_id":"serde_derive","kind":"normal"},{"crate_id":"serde_core","kind":"normal"}]}"""

                else ->
                    """{"crate":{"name":"serde","max_version":"1.0.228","description":"d","downloads":9},
                        "versions":[{"num":"1.0.228","downloads":5,"created_at":"2025-09-01T00:00:00Z"}]}"""
            }
            respond(body, HttpStatusCode.OK, jsonHeaders)
        }
        val detail = api.getCrate("serde")
        assertEquals("serde", detail.crate.name)
        assertEquals(listOf("serde_derive", "serde_core"), detail.dependencies)
        assertEquals(listOf("dtolnay"), detail.crate.owners)
        assertEquals("David Tolnay", detail.owners.single().name)
        assertEquals("1.0.228", detail.versions.single().num)
        assertNotNull(detail.versions.single().createdAt)
    }
}
