package data

expect class FavoritesStorage() {
    fun load(): List<String>
    fun save(favorites: List<String>)
}
