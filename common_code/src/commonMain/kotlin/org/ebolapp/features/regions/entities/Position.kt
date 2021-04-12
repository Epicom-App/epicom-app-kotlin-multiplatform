package org.ebolapp.features.regions.entities

import kotlinx.serialization.Serializable

@Serializable
data class Position(
    val lat: Double,
    val lon: Double
) {
    fun toString(separator: String): String {
        return "$lat$separator$lon"
    }

    companion object {
        fun create(value: String, separator: String): Position? {
            return value
                .split(separator)
                .mapNotNull { it.toDoubleOrNull() }
                .let { if (it.count() == 2) Position(it[0], it[1]) else null }
        }
    }
}