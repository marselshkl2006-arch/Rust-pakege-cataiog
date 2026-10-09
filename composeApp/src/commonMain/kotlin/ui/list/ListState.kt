package ui.list

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.CratesRepository
import data.DEFAULT_PER_PAGE
import data.toUserMessage
import kotlin.coroutines.cancellation.CancellationException
import model.Crate

fun List<Crate>.filterByQuery(query: String): List<Crate> {
    val q = query.trim()
    if (q.isEmpty()) return this
    return filter { it.name.contains(q, ignoreCase = true) || it.description.orEmpty().contains(q, ignoreCase = true) }
}

@Stable
class ListState {
    var crates by mutableStateOf<List<Crate>>(emptyList())
    var query by mutableStateOf("")
    var page by mutableIntStateOf(1)
    var isLoading by mutableStateOf(false)
    var endReached by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    val filtered: List<Crate> get() = crates.filterByQuery(query)

    private var generation = 0
    private var loadedKey: String? = null

    fun needsReload(useMocks: Boolean): Boolean = loadedKey != keyFor(useMocks) || error != null

    suspend fun loadFirstPage(repository: CratesRepository, useMocks: Boolean) {
        val gen = ++generation
        loadedKey = keyFor(useMocks)
        crates = emptyList()
        page = 1
        endReached = false
        error = null
        isLoading = true
        val result = try {
            repository.search(query, 1, DEFAULT_PER_PAGE)
        } catch (e: CancellationException) {
            if (gen == generation) {
                isLoading = false
                loadedKey = null
            }
            throw e
        }
        if (gen != generation) return
        result
            .onSuccess {
                crates = it.distinctBy(Crate::name)
                endReached = it.size < DEFAULT_PER_PAGE
            }
            .onFailure { error = it.toUserMessage() }
        isLoading = false
    }

    suspend fun loadNextPage(repository: CratesRepository) {
        if (isLoading || endReached || error != null) return
        val gen = generation
        val next = page + 1
        isLoading = true
        val result = try {
            repository.search(query, next, DEFAULT_PER_PAGE)
        } catch (e: CancellationException) {
            if (gen == generation) isLoading = false
            throw e
        }
        if (gen != generation) return
        result
            .onSuccess {
                crates = (crates + it).distinctBy(Crate::name)
                page = next
                endReached = it.size < DEFAULT_PER_PAGE
            }
            .onFailure { error = it.toUserMessage() }
        isLoading = false
    }

    private fun keyFor(useMocks: Boolean) = "${query.trim()}|$useMocks"
}
