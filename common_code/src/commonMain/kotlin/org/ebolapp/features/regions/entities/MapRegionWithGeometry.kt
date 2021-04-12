package org.ebolapp.features.regions.entities

data class MapRegionWithGeometry(
    val id: String,
    val parentId: String?,
    val severity: Int,
    val color: String,
    val geoRings: List<List<Position>>
) {

    override fun equals(other: Any?): Boolean {
        return hashCode() == other?.hashCode()
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (parentId?.hashCode() ?: 0)
        result = 31 * result + severity
        result = 31 * result + color.hashCode()
        return result
    }
}