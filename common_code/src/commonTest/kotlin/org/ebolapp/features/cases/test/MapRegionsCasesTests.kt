package org.ebolapp.features.cases.test

import io.ktor.util.date.*
import kotlinx.coroutines.runBlocking
import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.cases.test.data.*
import org.ebolapp.shared.network.NoParametersStrategy
import kotlin.test.Test
import kotlin.test.assertEquals


class MapRegionsCasesTests {

    private fun mapRegionsCasesTestUseCaseFactory(
        endpoints: Endpoints,
        mockNetworkApiResponseHandler: MockHttpRequestHandler = {
            mapRegionsCasesMockResponseHandler(it)
        },
        mockCasesDbApi: CasesDbApi = MockRegionCasesDbApi()
    ) : MapRegionsCasesTestUseCaseFactory {
        return MapRegionsCasesTestUseCaseFactory(
            endpoints = endpoints,
            requestHandler = mockNetworkApiResponseHandler,
            mockCasesDbApi = mockCasesDbApi
        )
    }

    @Test
    fun parse_region_cases_int_test() = runBlocking {

        val riskAreaCasesTestFactory = mapRegionsCasesTestUseCaseFactory(
            Endpoints(
                mapStatesCases = NoParametersStrategy(""),
                mapRegionCases = NoParametersStrategy(MapRegionsCasesTestsData.ScenarioParseRegionCasesInt.url),
                mapRegionCasesLegend = NoParametersStrategy("")
            )
        )

        // Test subject
        val getRiskAreaCasesUseCase = riskAreaCasesTestFactory.createGetMapRegionCasesByTimestampUseCase()

        // Test result
        val riskAreaCasesExpect = MapRegionsCasesTestsData.ScenarioParseRegionCasesInt.expect
        val riskAreaCasesActual = getRiskAreaCasesUseCase(
            timestampSec = GMTDate().timestamp
        )

        // Test result comparision
        assertEquals(riskAreaCasesExpect, riskAreaCasesActual)
    }

    @Test
    fun parse_region_cases_float_test()= runBlocking {

        val riskAreaCasesTestFactory = mapRegionsCasesTestUseCaseFactory(
            Endpoints(
                mapStatesCases = NoParametersStrategy(""),
                mapRegionCases = NoParametersStrategy(MapRegionsCasesTestsData.ScenarioParseRegionCasesFloat.url),
                mapRegionCasesLegend = NoParametersStrategy("")
            )
        )

        // Test subject
        val getRiskAreaCasesUseCase = riskAreaCasesTestFactory.createGetMapRegionCasesByTimestampUseCase()

        // Test result
        val riskAreaCasesExpect = MapRegionsCasesTestsData.ScenarioParseRegionCasesFloat.expect
        val riskAreaCasesActual = getRiskAreaCasesUseCase(
                timestampSec = GMTDate().timestamp
        )

        // Test result comparision
        assertEquals(riskAreaCasesExpect, riskAreaCasesActual)
    }

    @Test
    fun parse_region_cases_legend_test() = runBlocking {

        val testUrl = MapRegionsCasesTestsData.ScenarioParseRegionCasesLegend.url

        val riskAreaCasesTestFactory = mapRegionsCasesTestUseCaseFactory(
            Endpoints(
                mapStatesCases = NoParametersStrategy(""),
                mapRegionCases = NoParametersStrategy(""),
                mapRegionCasesLegend = NoParametersStrategy(testUrl)
            ),
            mockCasesDbApi = object : MockRegionCasesDbApi() {
                var mapRegionLegend: MapRegionLegend? = null
                override suspend fun loadMapRegionLegend(): MapRegionLegend? = mapRegionLegend
                override suspend fun saveMapRegionLegend(legend: MapRegionLegend) { mapRegionLegend = legend }
                override suspend fun deleteMapRegionLegend() { mapRegionLegend = null }
            }
        )

        // Test subject
        val getRiskAreaCasesLegendUseCase = riskAreaCasesTestFactory.createGetMapRegionCasesLegendUseCase()

        // Test result
        val riskAreaCasesLegendExpect = MapRegionsCasesTestsData.ScenarioParseRegionCasesLegend.expect
        val riskAreaCasesLegendActual = getRiskAreaCasesLegendUseCase()

        // Test result comparision
        assertEquals(riskAreaCasesLegendExpect, riskAreaCasesLegendActual)
    }
}