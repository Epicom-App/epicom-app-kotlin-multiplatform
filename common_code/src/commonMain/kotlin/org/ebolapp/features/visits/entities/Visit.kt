package org.ebolapp.features.visits.entities

import kotlinx.serialization.Serializable
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.utils.DateUtils

@Serializable
data class Visit(
    val id: Long = -1,
    val position: Position,
    val startTimestamp: Long = 0,
    val endTimestamp: Long = 0
) {
    fun durationSec() : Long = endTimestamp - startTimestamp
    companion object {
        const val MIN_VISIT_DURATION_SEC = 900L // 15 minutes
    }
}
