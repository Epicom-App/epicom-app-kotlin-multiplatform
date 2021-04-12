package org.ebolapp.features.regions.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapStateCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.test.data.GetMapRegionsTestsData
import org.ebolapp.features.regions.test.data.MockRegionsDbApi
import org.ebolapp.features.regions.usecases.GetMapRegionsUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class GetMapRegionsTests {

    @Test
    fun get_map_regions_positive() = runBlocking {
        // Test data
        val testData = GetMapRegionsTestsData.GetMapRegionsPositive
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            override suspend fun getMapRegions(
                regionBox: MapRegionBox,
                statesOnly: Boolean
            ): List<MapRegionTable> {
                return listOf(testData.mapRegionTable)
            }
        }
        val getMapRegionCasesLegendUseCase = object: GetMapRegionCasesLegendUseCase {
            override suspend fun invoke(): MapRegionLegend {
                return testData.mapRegionLegend
            }
        }
        val getMapRegionCasesByTimestampUseCase = object: GetMapRegionCasesByTimestampUseCase {
            override suspend fun invoke(
                timestampSec: Long
            ): List<MapRegionCase> {
                return testData.mapRegionCases[timestampSec] ?: emptyList()
            }
        }
        val getMapStatesCasesByTimestampUseCase = object: GetMapStatesCasesByTimestampUseCase {
            override suspend fun invoke(
                timestampSec: Long
            ): List<MapStateCase> = emptyList()
        }

        // test subjects
        val getMapRegionsUseCase = GetMapRegionsUseCaseImpl(
            databaseApi = mockRegionsDbApi,
            getMapRegionCasesLegendUseCase = getMapRegionCasesLegendUseCase,
            getMapStatesCasesByTimestampUseCase = getMapStatesCasesByTimestampUseCase,
            getMapRegionCasesByTimestampUseCase = getMapRegionCasesByTimestampUseCase
        )

        // test
        val mapRegionsResult = getMapRegionsUseCase(
            mapRegionBox = testData.mapRegionBox,
            timestampSec = testData.timestampSec
        )

        // test results

        assertTrue { mapRegionsResult.size == 1 }
        assertTrue { mapRegionsResult[0].id == "01001"}
        assertTrue { mapRegionsResult[0].severity == 4}
        assertTrue { mapRegionsResult[0].color == "#A33438"}
    }

}