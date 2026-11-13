package data

import model.Crate
import model.CrateDetail
import model.Owner
import kotlin.coroutines.cancellation.CancellationException

class CratesRepository(
    private val api: CratesDataSource
) {
    private val knownOwners = mutableMapOf<String, Owner>()

    suspend fun search(query: String, page: Int, perPage: Int = DEFAULT_PER_PAGE): Result<List<Crate>> =
        call { api.search(query, page, perPage) }

    suspend fun getCrate(name: String): Result<CrateDetail> = call {
        api.getCrate(name).also { detail -> detail.owners.forEach { knownOwners[it.login] = it } }
    }

    suspend fun getDependencies(name: String): Result<List<String>> = call { api.getDependencies(name) }

    suspend fun getOwners(name: String): Result<List<Owner>> = call {
        api.getOwners(name).also { list -> list.forEach { knownOwners[it.login] = it } }
    }

    suspend fun getOwner(login: String): Result<Owner> = call {
        knownOwners[login] ?: api.getUser(login).also { knownOwners[login] = it }
    }

    private suspend inline fun <T> call(block: () -> T): Result<T> = try {
        Result.success(block())
    } catch (e: CancellationException) {
        throw e
    } catch (e: Throwable) {
        Result.failure(e)
    }
}
