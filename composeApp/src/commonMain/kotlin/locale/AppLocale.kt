package locale

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key


@Composable
fun LocalizedContent(language: String, content: @Composable () -> Unit) {
    applyAppLanguage(language)
    key(language) { content() }
}
