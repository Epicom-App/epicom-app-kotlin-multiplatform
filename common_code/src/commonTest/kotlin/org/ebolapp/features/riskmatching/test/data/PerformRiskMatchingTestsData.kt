package org.ebolapp.features.riskmatching.test.data

import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapRegionLegendItem
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit


object PerformRiskMatchingTestsData {

    object PerformRiskMatchingPositive {

        val nowRiskMatchTimestamp = 1605567600L // 11/16/2020 @ 11:00pm (UTC)
        val lastRiskMatchTimestamp = 1605524000L //11/16/2020 @ 10:53am (UTC)

        val visits = listOf(
            Visit(
                id = 0,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1605524400, // 11/16/2020 @ 11:00am (UTC),
                endTimestamp = 1605531600, // 11/16/2020 @ 1:00pm (UTC)
            ),
            Visit(
                id = 1,
                position = Position(
                    lat = 55.222222,
                    lon = 11.222222
                ),
                startTimestamp = 1605531600, // 11/16/2020 @ 1:00pm (UTC),
                endTimestamp = 1605542400, // 11/16/2020 @ 4:00pm (UTC)
            )
        )

        val mapRegionId = mapOf(
            visits[0].position to "regionId_1",
            visits[1].position to "regionId_2"
        )

        val mapRegionInfo = mapOf(
            "regionId_1" to MapRegionInfo(
                id = "regionId_1",
                name = "regionId_name",
                severity = 3,
                maxSeverity = 5,
                isRisky = true,
                severityInfo = "> 25 ≤ 50",
                color = "#C69638",
                casesNumber = 30.0f,
                casesNumberInfo = "Cases Number Info",
                disease = "covid",
                informationUrl = null
            ),
            "regionId_2" to MapRegionInfo(
                id = "regionId_2",
                name = "regionId_name",
                severity = 4,
                maxSeverity = 5,
                isRisky = true,
                severityInfo = "> 50 ≤ 100",
                color = "#A33438",
                casesNumber = 55.0f,
                casesNumberInfo = "Cases Number Info",
                disease = "covid",
                informationUrl = null
            )
        )
    }

    object PerformRiskMatchingPositiveCross3Days {

        val nowRiskMatchTimestamp = 1606834800L // 12/01/2020 @ 3:00pm (UTC)
        val lastRiskMatchTimestamp = 1606611600L // 11/29/2020 @ 1:00am (UTC)

        val visits = listOf(
            Visit(
                id = 0,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1606618800L, // 11/29/2020 @ 3:00am (UTC)
                endTimestamp = 1606824000L, // 12/01/2020 @ 12:00pm (UTC)
            )
        )

        val mapRegionId = mapOf(
            visits[0].position to "regionId_1"
        )

        val mapRegionInfo = mapOf(
            "regionId_1" to MapRegionInfo(
                id = "regionId_1",
                name = "regionId_name",
                severity = 3,
                maxSeverity = 5,
                isRisky = true,
                severityInfo = "> 25 ≤ 50",
                color = "#C69638",
                casesNumber = 30.0f,
                casesNumberInfo = "Cases Number Info",
                disease = "covid",
                informationUrl = null
            )
        )
    }

    object PerformRiskMatchingMultipleVisitsAcross3Days {

        val nowRiskMatchTimestamp = 1606834800L // 12/01/2020 @ 3:00pm (UTC)
        val lastRiskMatchTimestamp = 1606611600L // 11/29/2020 @ 1:00am (UTC)

        val visits = listOf(
            Visit(
                id = 1,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1606618800L, // 11/29/2020 @ 3:00am (UTC)
                endTimestamp = 1606629600L, // 11/29/2020 @ 6:00am (UTC)
            ),
            Visit(
                id = 2,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1606629600L, // 11/29/2020 @ 6:00am (UTC)
                endTimestamp = 1606734000L, // 11/30/2020 @ 11:00am (UTC)
            ),
            Visit(
                id = 3,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1606734000L, // 11/30/2020 @ 11:00am (UTC)
                endTimestamp = 1606809600L, // 12/01/2020 @ 8:00am (UTC)
            ),
            Visit(
                id = 4,
                position = Position(
                    lat = 53.111111,
                    lon = 14.111111
                ),
                startTimestamp = 1606813200L, // 12/01/2020 @ 9:00am (UTC)
                endTimestamp = 1606824000L, // 12/01/2020 @ 12:00pm (UTC)
            )
        )

        val mapRegionId = mapOf(
            visits[0].position to "regionId_1",
            visits[1].position to "regionId_1",
            visits[2].position to "regionId_1",
            visits[3].position to "regionId_1",
        )

        val mapRegionInfo = mapOf(
            "regionId_1" to MapRegionInfo(
                id = "regionId_1",
                name = "regionId_name",
                severity = 3,
                maxSeverity = 5,
                isRisky = true,
                severityInfo = "> 25 ≤ 50",
                color = "#C69638",
                casesNumber = 30.0f,
                casesNumberInfo = "Cases Number Info",
                disease = "covid",
                informationUrl = null
            )
        )
    }
}