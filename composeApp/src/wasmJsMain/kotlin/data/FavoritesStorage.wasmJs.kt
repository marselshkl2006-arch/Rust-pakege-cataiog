package data

import kotlinx.browser.localStorage

actual class FavoritesStorage {
    private val key = "favorites"

    actual fun load(): List<String> {
        val data = localStorage.getItem(key) ?: return emptyList()
        return if (data.isBlank()) emptyList() else data.split("\n")
    }

    actual fun save(favorites: List<String>) {
        localStorage.setItem(key, favorites.joinToString("\n"))
    }
}
