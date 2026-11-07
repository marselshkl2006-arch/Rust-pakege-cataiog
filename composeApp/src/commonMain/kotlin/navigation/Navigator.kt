package navigation

import androidx.compose.runtime.Stable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.mutableStateListOf

/** Thin wrapper over the Navigation 3 back stack (a plain snapshot list, kept in memory only). */
@Stable
class Navigator(start: Screen = Screen.List) {
    val backStack: SnapshotStateList<Screen> = mutableStateListOf(start)

    fun navigateTo(screen: Screen) {
        backStack.add(screen)
    }

    fun goBack() {
        if (backStack.size > 1) backStack.removeAt(backStack.lastIndex)
    }
}
