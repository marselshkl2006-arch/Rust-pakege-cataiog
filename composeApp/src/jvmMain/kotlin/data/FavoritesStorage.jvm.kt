package data

import java.io.File

actual class FavoritesStorage {
    private val file = File("favorites.txt")

    actual fun load(): List<String> {
        return if (file.exists()) {
            file.readLines().filter { it.isNotBlank() }
        } else {
            emptyList()
        }
    }

    actual fun save(favorites: List<String>) {
        file.writeText(favorites.joinToString("\n"))
    }
}
