package org.ebolapp.features.regions.entities

data class MapRegion(
    val id: String,
    val parentId: String?,
    val severity: Int,
    val color: String
)