package ui.detail

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.CratesRepository
import data.toUserMessage
import model.CrateDetail

@Stable
class DetailState {
    var crate by mutableStateOf<CrateDetail?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    suspend fun load(repository: CratesRepository, name: String) {
        isLoading = true
        error = null
        repository.getCrate(name)
            .onSuccess { crate = it }
            .onFailure { error = it.toUserMessage() }
        isLoading = false
    }
}
