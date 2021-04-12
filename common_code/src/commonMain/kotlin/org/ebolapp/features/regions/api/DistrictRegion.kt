package org.ebolapp.features.regions.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.ebolapp.features.regions.entities.Position

@Serializable
data class DistrictRegion(
    @SerialName("areaID")
    val id: String,
    @SerialName("bundeslandID")
    val stateId: String,
    @SerialName("areaName")
    val name: String,
    val geoRing: List<List<Position>>
)
