package locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key

/** Makes the platform resource lookup use [language] ("ru" / "en"). Idempotent. */
expect fun applyAppLanguage(language: String)

/**
 * Re-creates [content] when the language changes so every stringResource() is resolved again.
 * All app state is held above this call, so switching language does not lose it.
 */
@Composable
fun LocalizedContent(language: String, content: @Composable () -> Unit) {
    applyAppLanguage(language)
    key(language) { content() }
}
