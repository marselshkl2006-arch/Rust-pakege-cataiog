package navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

/**
 * Type-safe routes. They are nested in [Screen] so that the names from the spec (List, Owner...)
 * never clash with kotlin.collections.List or model.Owner: always refer to them as Screen.X.
 */
@Serializable
sealed interface Screen : NavKey {
    @Serializable
    data object List : Screen

    @Serializable
    data class Detail(val crateName: String) : Screen

    @Serializable
    data class Dependencies(val crateName: String) : Screen

    @Serializable
    data class Owner(val login: String) : Screen
}
