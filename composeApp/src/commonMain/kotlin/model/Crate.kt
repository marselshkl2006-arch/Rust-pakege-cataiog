package model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Field names follow the crates.io API. `dependencies` and `owners` are not part of the
 * search/detail payload: they are filled in by the data layer from separate endpoints.
 */
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
