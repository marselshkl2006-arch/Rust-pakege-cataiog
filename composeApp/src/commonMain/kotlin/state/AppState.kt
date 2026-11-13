package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class AppState(
    isDarkTheme: Boolean = false,
    language: String = "ru",
) {
    var isDarkTheme by mutableStateOf(isDarkTheme)
        private set

    var language by mutableStateOf(language)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }

    fun changeLanguage(language: String) {
        require(language in SUPPORTED_LANGUAGES) { "Unsupported language: $language" }
        this.language = language
    }

    companion object {
        val SUPPORTED_LANGUAGES = listOf("ru", "en")
    }
}
