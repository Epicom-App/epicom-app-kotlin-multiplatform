package org.ebolapp.features.cases.entities

import kotlinx.serialization.Serializable

@Serializable
data class MapRegionCase (
    val areaId: String,
    val stateId: Int?,
    val severity: Int,
    var numberOfCases: Float,
    val informationUrl: String? = null
)