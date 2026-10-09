package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@Stable
class AppState(
    isDarkTheme: Boolean = false,
    language: String = "ru",
    useMocks: Boolean = true,
) {
    var isDarkTheme by mutableStateOf(isDarkTheme)
        private set

    /** "ru" or "en". */
    var language by mutableStateOf(language)
        private set

    /** true = hand-written MockData, false = real crates.io API. */
    var useMocks by mutableStateOf(useMocks)
        private set

    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
    }

    
    fun changeLanguage(language: String) {
        require(language in SUPPORTED_LANGUAGES) { "Unsupported language: $language" }
        this.language = language
    }

    fun toggleDataSource() {
        useMocks = !useMocks
    }

    companion object {
        val SUPPORTED_LANGUAGES = listOf("ru", "en")
    }
}
