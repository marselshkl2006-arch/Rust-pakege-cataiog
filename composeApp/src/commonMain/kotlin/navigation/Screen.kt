package navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable


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
