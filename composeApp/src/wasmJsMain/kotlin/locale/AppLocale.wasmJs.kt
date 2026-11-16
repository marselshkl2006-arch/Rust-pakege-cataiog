package locale

actual fun applyAppLanguage(language: String) {
    overrideNavigatorLanguage(language)
}

// Compose resources read the browser language on web; override what navigator reports.
private fun overrideNavigatorLanguage(lang: String): Unit = js(
    """{
        try {
            Object.defineProperty(navigator, 'language', { get: function() { return lang; }, configurable: true });
            Object.defineProperty(navigator, 'languages', { get: function() { return [lang]; }, configurable: true });
        } catch (e) {}
    }"""
)
