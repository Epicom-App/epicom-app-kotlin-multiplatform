package org.ebolapp.features.regions.api

import kotlinx.serialization.Serializable
import org.ebolapp.features.regions.entities.Position

@Serializable
data class StateRegion(
    val id: String,
    val name: String,
    val geoRing: List<List<Position>>
)

