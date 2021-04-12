package org.ebolapp.features.regions.test.data

import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.regions.entities.Position

object GetMapRegionByPositionTestsData {

    object GetMapRegionByPosition {
        val regionPosition = Position(
            lat = 51.334184,
            lon = 11.228447
        )
        val testMapRegionTable = MapRegionTable(
            id = "01001",
            name = "Lipzig",
            parentId = "1",
            topLeftLat = 51.335782,
            topLeftLon = 11.231100,
            bottomRightLat = 51.334001,
            bottomRightLon = 11.222222
        )
        val testMapRegionGeometryData = listOf(
            Position(
                lat = 51.333444,
                lon = 11.222222
            ),
            Position(
                lat = 51.335371,
                lon = 11.222928
            ),
            Position(
                lat = 51.335782,
                lon = 11.229354
            ),
            Position(
                lat = 51.334001,
                lon = 11.231100
            ),
            Position(
                lat = 51.333444,
                lon = 11.222222
            )
        )
    }

}