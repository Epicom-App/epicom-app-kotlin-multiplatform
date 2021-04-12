package org.ebolapp.features.cases.entities

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapRegionLegend (
    val name: String,
    val items: List<MapRegionLegendItem>
)

@Serializable
data class MapRegionLegendItem(
    val severity: Int,
    @SerialName("description")
    val info: String,
    val color: String,
    var isRisky: Boolean
)