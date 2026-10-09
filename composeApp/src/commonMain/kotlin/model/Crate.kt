package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Crate(
    val name: String,
    @SerialName("max_version") val version: String,
    val description: String? = null,
    val downloads: Long = 0,
    val repository: String? = null,
    val documentation: String? = null,
    val homepage: String? = null,
    val dependencies: List<String> = emptyList(),
    val owners: List<String> = emptyList(),
)
