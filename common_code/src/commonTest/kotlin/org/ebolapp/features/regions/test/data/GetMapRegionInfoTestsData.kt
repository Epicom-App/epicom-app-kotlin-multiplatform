package org.ebolapp.features.regions.test.data

import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapRegionLegendItem
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.Position

object GetMapRegionInfoTestsData {

    object GetMapRegionInfo {

        val testSearchRegionId = "01001"

        val timestampSec = 1605884400L // 11/20/2020 @ 3:00pm (UTC)


        val mapRegionLegend = MapRegionLegend(
            name = "Fälle der letzten 7 Tage/100.000 Einwohner",
            listOf(
                MapRegionLegendItem(severity = 0, info = "0", color = "#D3D3D3", isRisky = false),
                MapRegionLegendItem(severity = 1, info = "> 0 ≤ 5", color = "#D3CFAF", isRisky = false),
                MapRegionLegendItem(severity = 2, info = "> 5 ≤ 25", color = "#D2CD8D", isRisky = false),
                MapRegionLegendItem(severity = 3, info = "> 25 ≤ 50", color = "#C69638", isRisky = true),
                MapRegionLegendItem(severity = 4, info = "> 50 ≤ 100", color = "#A33438", isRisky = true),
                MapRegionLegendItem(severity = 5, info = "> 100 ≤ 500", color = "#821F20", isRisky = true)
            )
        )

        val mapRegionCases : Map<Long,List<MapRegionCase>> = mapOf(
            timestampSec to listOf(
                MapRegionCase(
                    areaId = "01001",
                    stateId = 0,
                    severity = 4,
                    numberOfCases = 75f,
                    informationUrl = null
                ),
                MapRegionCase(
                    areaId = "01002",
                    stateId = 0,
                    severity = 5,
                    numberOfCases = 120f,
                    informationUrl = null
                )
            )
        )

        val mapRegionBox = MapRegionBox(
            topLeft = Position(lat = 32.3232, lon = 33.3232),
            bottomRight = Position(lat = 34.2322, lon = 33.2232)
        )

        val mapRegionTable = mapOf<String,MapRegionTable>(
            "01001" to MapRegionTable(
                id = "01001",
                name = "Lipzig",
                parentId = "1",
                topLeftLat = mapRegionBox.topLeft.lat,
                topLeftLon = mapRegionBox.topLeft.lon,
                bottomRightLat = mapRegionBox.bottomRight.lat,
                bottomRightLon = mapRegionBox.bottomRight.lon
        ))
    }

}