package org.ebolapp.features.regions.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapStateCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.test.data.GetMapRegionInfoTestsData
import org.ebolapp.features.regions.test.data.MockRegionsDbApi
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class GetMapRegionInfoTests {

    @Test
    fun get_map_region_info_test_positive() = runBlocking {

        // test data
        val testData = GetMapRegionInfoTestsData.GetMapRegionInfo
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            override suspend fun getMapRegion(regionId: String): MapRegionTable {
                return testData.mapRegionTable[regionId] ?: throw Exception("regionId not found")
            }
        }
        val getMapRegionCasesLegendUseCase = object: GetMapRegionCasesLegendUseCase {
            override suspend fun invoke(): MapRegionLegend {
                return testData.mapRegionLegend
            }
        }
        val getMapRegionCasesUseCaseImpl = object: GetMapRegionCasesByTimestampUseCase {
            override suspend fun invoke(
                timestampSec: Long
            ): List<MapRegionCase> {
                return testData.mapRegionCases[timestampSec] ?: emptyList()
            }
        }
        val getMapStatesCasesUseCaseImpl = object: GetMapStatesCasesByTimestampUseCase {
            override suspend fun invoke(
                timestampSec: Long
            ): List<MapStateCase> = emptyList()
        }

        val getMapRegionInfoUseCase = GetMapRegionInfoUseCaseImpl(
            regionsDbApi = mockRegionsDbApi,
            getMapRegionCasesUseCase = getMapRegionCasesUseCaseImpl,
            getMapStatesCasesUseCase = getMapStatesCasesUseCaseImpl,
            getMapRegionCasesLegendUseCase = getMapRegionCasesLegendUseCase
        )

        val mapRegionInfo = getMapRegionInfoUseCase(
            regionId = testData.testSearchRegionId,
            timestampSec = testData.timestampSec
        )


        assertTrue { mapRegionInfo?.id == testData.testSearchRegionId }
    }

}