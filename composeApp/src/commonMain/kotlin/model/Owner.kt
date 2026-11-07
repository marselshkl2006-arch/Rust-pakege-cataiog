package model

import kotlinx.serialization.Serializable

@Serializable
data class Owner(
    val login: String,
    val name: String? = null,
    val avatar: String? = null,
)
