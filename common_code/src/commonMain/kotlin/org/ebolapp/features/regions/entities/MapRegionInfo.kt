package org.ebolapp.features.regions.entities

data class MapRegionInfo(
    val id: String,
    val name: String,
    val severity: Int,
    val maxSeverity: Int,
    val isRisky: Boolean,
    val severityInfo: String,
    val color: String,
    val casesNumber: Float,
    val casesNumberInfo: String,
    val disease: String,
    val informationUrl: String?
)