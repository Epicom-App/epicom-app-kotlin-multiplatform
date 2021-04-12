package org.ebolapp.utils

import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.Position
import kotlin.math.*

object MapUtils {

    // Solution: https://www.geeksforgeeks.org/program-distance-two-points-earth/
    fun distanceBetweenInKm(from: Position, to: Position): Double {
        // Convert the latitudes
        // and longitudes
        // from degree to radians.
        val lat1 = toRadians(from.lat)
        val lon1 = toRadians(from.lon)
        val lat2 = toRadians(to.lat)
        val lon2 = toRadians(to.lon)

        // Haversine Formula
        val dlong = lon2 - lon1
        val dlat = lat2 - lat1

        9.0.pow(1.0)
        var ans = sin(dlat / 2).pow(2) +
                  cos(lat1) * cos(lat2) *
                  sin(dlong / 2).pow(2)

        ans = 2 * asin(sqrt(ans))

        // Radius of Earth in
        // Kilometers, R = 6371
        // Use R = 3956 for miles
        val earthRadiusKm: Double = 6371.0

        // Calculate and return the result
        return ans * earthRadiusKm
    }

    fun distanceBetweenInMeters(from: Position, to: Position) : Double {
        return distanceBetweenInKm(from, to) * 1000.0
    }

    fun pointBelongsToBox(point: Position, mapRegionBox: MapRegionBox) : Boolean {
        return mapRegionBox.topLeft.lon <= point.lon &&
                point.lon <= mapRegionBox.bottomRight.lon &&
                mapRegionBox.topLeft.lat >= point.lat &&
                point.lat >= mapRegionBox.bottomRight.lat
    }

    fun calculateMapRegionBox(geoRings: List<List<Position>>): MapRegionBox {
        var southLat = Int.MAX_VALUE.toDouble()
        var westLon = Int.MAX_VALUE.toDouble()
        var northLat = Int.MIN_VALUE.toDouble()
        var eastLon = Int.MIN_VALUE.toDouble()
        for (ring in geoRings) {
            for (position in ring) {
                southLat = min(southLat, position.lat)
                westLon = min(westLon, position.lon)
                northLat = max(northLat, position.lat)
                eastLon = max(eastLon, position.lon)
            }
        }
        return MapRegionBox(
            topLeft = Position(lat = northLat, lon = westLon),
            bottomRight = Position(lat = southLat, lon = eastLon)
        )
    }

    private fun toRadians(value: Double) : Double {
        return PI / 180.0 * value
    }
}