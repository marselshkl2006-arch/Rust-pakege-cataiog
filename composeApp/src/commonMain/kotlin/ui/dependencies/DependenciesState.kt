package ui.dependencies

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.CratesRepository
import data.toUserMessage

@Stable
class DependenciesState {
    var dependencies by mutableStateOf<List<String>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    suspend fun load(repository: CratesRepository, crateName: String) {
        isLoading = true
        error = null
        repository.getDependencies(crateName)
            .onSuccess { dependencies = it }
            .onFailure { error = it.toUserMessage() }
        isLoading = false
    }
}
