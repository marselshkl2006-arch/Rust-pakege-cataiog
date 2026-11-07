package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CrateVersion(
    val num: String,
    val downloads: Long = 0,
    @SerialName("created_at") val createdAt: String = "",
)
