package org.ebolapp.features.cases.entities

import kotlinx.serialization.Serializable

@Serializable
data class MapStateCase (
    val informationUrl: String? = null,
    val stateId: Int,
    val severity: Int,
    var numberOfCases: Float
)