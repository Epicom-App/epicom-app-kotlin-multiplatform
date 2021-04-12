package org.ebolapp.features.cases.test.data

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapRegionLegendItem


class MapRegionsCasesTestsData {

    object ScenarioParseRegionCasesInt {
        val url = "/ScenarioParseRegionCasesInt"
        val actualJson = """
            [{
                "areaId": "LEI",
                "stateId": 0,
                "severity": 0,
                "numberOfCases": 6
             },
             {
                "areaId": "MUN",
                "stateId": 0,
                "severity": 3,
                "numberOfCases": 55
            }]
            """
        val expect = listOf(
            MapRegionCase(areaId = "LEI", stateId = 0, severity = 0, numberOfCases = 6f, informationUrl = null),
            MapRegionCase(areaId = "MUN", stateId = 0,  severity = 3, numberOfCases = 55f, informationUrl = null)
        )
    }

    object ScenarioParseRegionCasesFloat {
        val url = "/ScenarioParseRegionCasesFloat"
        val actualJson = """
            [{
                "areaId": "LEI",
                "stateId": 0,
                "severity": 0,
                "numberOfCases": 6.5
             },
             {
                "areaId": "MUN",
                "stateId": 0,
                "severity": 3,
                "numberOfCases": 55
            }]
            """
        val expect = listOf(
            MapRegionCase(areaId = "LEI", stateId = 0,  severity = 0, numberOfCases = 6.5f, informationUrl = null),
            MapRegionCase(areaId = "MUN", stateId = 0,  severity = 3, numberOfCases = 55f, informationUrl = null)
        )
    }

    object  ScenarioParseRegionCasesLegend {
        val url = "/ScenarioParseRegionCasesLegend"
        val actualJson = """
            {
              "name": "Fälle der letzten 7 Tage/100.000 Einwohner",
              "items": [
                {
                  "severity": 0,
                  "description": "0",
                  "color": "#D3D3D3",
                  "isRisky": false
                },
                {
                  "severity": 1,
                  "description": "> 0 ≤ 5",
                  "color": "#D3CFAF",
                  "isRisky": false
                },
                {
                  "severity": 2,
                  "description": "> 5 ≤ 25",
                  "color": "#D2CD8D",
                  "isRisky": false
                },
                {
                  "severity": 3,
                  "description": "> 25 ≤ 50",
                  "color": "#C69638",
                  "isRisky": true
                },
                {
                  "severity": 4,
                  "description": "> 50 ≤ 100",
                  "color": "#A33438",
                  "isRisky": true
                },
                {
                  "severity": 5,
                  "description": "> 100 ≤ 500",
                  "color": "#821F20",
                  "isRisky": true
                }
              ]
            }
        """.trimIndent()
        val expect = MapRegionLegend(
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
    }

}


typealias MockHttpRequestHandler = suspend MockRequestHandleScope.(request: HttpRequestData) -> HttpResponseData

fun MockRequestHandleScope.mapRegionsCasesMockResponseHandler(
    request: HttpRequestData
): HttpResponseData {
    val contentTypeJsonHeader =
        headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
    return when (request.url.fullPath) {
        MapRegionsCasesTestsData.ScenarioParseRegionCasesInt.url ->
            respond(MapRegionsCasesTestsData.ScenarioParseRegionCasesInt.actualJson, headers = contentTypeJsonHeader)
        MapRegionsCasesTestsData.ScenarioParseRegionCasesFloat.url ->
            respond(MapRegionsCasesTestsData.ScenarioParseRegionCasesFloat.actualJson, headers = contentTypeJsonHeader)
        MapRegionsCasesTestsData.ScenarioParseRegionCasesLegend.url ->
            respond(MapRegionsCasesTestsData.ScenarioParseRegionCasesLegend.actualJson, headers = contentTypeJsonHeader)
        else -> error("Unhandled ${request.url.fullPath}")
    }
}