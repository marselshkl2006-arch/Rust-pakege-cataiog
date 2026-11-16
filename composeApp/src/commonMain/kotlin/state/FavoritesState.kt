package state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import data.FavoritesStorage

@Stable
class FavoritesState : ViewModel() {
    private val storage = FavoritesStorage()
    var favorites by mutableStateOf<List<String>>(emptyList())
        private set

    init {
        favorites = storage.load()
    }

    fun isFavorite(name: String) = favorites.contains(name)

    fun toggleFavorite(name: String) {
        favorites = if (favorites.contains(name)) {
            favorites - name
        } else {
            favorites + name
        }
        storage.save(favorites)
    }
}
