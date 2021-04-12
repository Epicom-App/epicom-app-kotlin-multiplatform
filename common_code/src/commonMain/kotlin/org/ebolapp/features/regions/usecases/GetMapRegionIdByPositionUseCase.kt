package org.ebolapp.features.regions.usecases

import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.entities.Position

interface GetMapRegionIdByPositionUseCase {
    suspend operator fun invoke(position: Position) : String?
}

internal class GetMapRegionIdByPositionUseCaseImpl(
    private val regionsDbApi: RegionsDbApi
) : GetMapRegionIdByPositionUseCase {

    override suspend fun invoke(position: Position): String? {
        val mapRegionsList = regionsDbApi.getMapRegionByPosition(position)
        mapRegionsList.forEach { mapRegion ->
            regionsDbApi.getMapRegionGeometry(mapRegion.id).forEach { polygon ->
                if (polygon.containsPosition(position))
                    return mapRegion.id
            }
        }
        return null
    }
}

private fun List<Position>.containsPosition(
    position: Position
): Boolean {

    if (size <= 2) return false

    var hits = 0

    var lastLatitude = last().lat
    var lastLongitude = last().lon
    var currentLatitude: Double
    var currentLongitude: Double

    // Walk the edges of the polygon
    var i = 0
    while (i < size) {
        currentLatitude = this[i].lat
        currentLongitude = this[i].lon

        if (currentLongitude == lastLongitude) {
            lastLatitude = currentLatitude
            lastLongitude = currentLongitude
            i++
            continue
        }

        val leftLatitude: Double
        if (currentLatitude < lastLatitude) {
            if (position.lat >= lastLatitude) {
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            leftLatitude = currentLatitude
        } else {
            if (position.lon >= currentLatitude) {
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            leftLatitude = lastLatitude
        }

        val test1: Double
        val test2: Double
        if (currentLongitude < lastLongitude) {
            if (position.lon < currentLongitude || position.lon >= lastLongitude) {
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            if (position.lat < leftLatitude) {
                hits++
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            test1 = position.lat - currentLatitude
            test2 = position.lon - currentLongitude
        } else {
            if (position.lon < lastLongitude || position.lon >= currentLongitude) {
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            if (position.lat < leftLatitude) {
                hits++
                lastLatitude = currentLatitude
                lastLongitude = currentLongitude
                i++
                continue
            }
            test1 = position.lat - lastLatitude
            test2 = position.lon - lastLongitude
        }

        if (test1 < test2 / (lastLongitude - currentLongitude) * (lastLatitude - currentLatitude)) {
            hits++
        }
        lastLatitude = currentLatitude
        lastLongitude = currentLongitude
        i++
    }

    return hits and 1 != 0
}