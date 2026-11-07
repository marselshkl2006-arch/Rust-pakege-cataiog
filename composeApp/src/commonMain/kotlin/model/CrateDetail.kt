package model

data class CrateDetail(
    val crate: Crate,
    val versions: List<CrateVersion>,
    val owners: List<Owner>,
    val dependencies: List<String>,
)
