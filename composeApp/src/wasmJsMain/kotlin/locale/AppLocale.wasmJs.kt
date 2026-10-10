package locale

import kotlin.js.ExperimentalWasmJsInterop

@OptIn(ExperimentalWasmJsInterop::class)
actual fun applyAppLanguage(language: String) {
    overrideNavigatorLanguage(language)
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun overrideNavigatorLanguage(lang: String): Unit = js(
    """{
        try {
            Object.defineProperty(navigator, 'language', { get: function() { return lang; }, configurable: true });
            Object.defineProperty(navigator, 'languages', { get: function() { return [lang]; }, configurable: true });
        } catch (e) {}
    }"""
)
