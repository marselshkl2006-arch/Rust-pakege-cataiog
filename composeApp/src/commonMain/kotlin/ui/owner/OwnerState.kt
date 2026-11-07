package ui.owner

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import data.CratesRepository
import data.toUserMessage
import model.Owner

@Stable
class OwnerState {
    var owner by mutableStateOf<Owner?>(null)
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    suspend fun load(repository: CratesRepository, login: String) {
        isLoading = true
        error = null
        repository.getOwner(login)
            .onSuccess { owner = it }
            .onFailure { error = it.toUserMessage() }
        isLoading = false
    }
}
