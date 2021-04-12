package org.ebolapp.features.regions.entities

class MapRegionBox(
    val topLeft: Position,
    val bottomRight: Position
) {
    override fun toString(): String {
        return listOf(topLeft, bottomRight).joinToString(separator = "::") { it.toString(";") }
    }
}