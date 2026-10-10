package locale

import java.util.Locale

actual fun applyAppLanguage(language: String) {
    Locale.setDefault(Locale.forLanguageTag(language))
}
