package org.ebolapp.features.visits.entities

import org.ebolapp.features.regions.entities.Position

data class UserLocation (
    val position: Position,
    val timestampSec: Long
)