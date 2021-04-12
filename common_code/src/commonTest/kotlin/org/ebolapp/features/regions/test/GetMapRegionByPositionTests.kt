package org.ebolapp.features.regions.test


import kotlinx.coroutines.runBlocking
import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.regions.test.data.GetMapRegionByPositionTestsData
import org.ebolapp.features.regions.test.data.MapRegionsTestUseCaseFactory
import org.ebolapp.features.regions.test.data.MockRegionsDbApi
import kotlin.test.Test
import kotlin.test.assertTrue

class GetMapRegionByPositionTests {

    @Test
    fun get_map_region_by_position_test() = runBlocking {

        // Test data
        val testData = GetMapRegionByPositionTestsData.GetMapRegionByPosition
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            override suspend fun getMapRegionByPosition(
                position: Position
            ): List<MapRegionTable> {
                return listOf(testData.testMapRegionTable)
            }
            override suspend fun getMapRegionGeometry(
                regionId: String
            ): List<List<Position>> {
                return listOf(testData.testMapRegionGeometryData)
            }
        }

        // test subjects

        val getMapRegionByPositionUseCase = MapRegionsTestUseCaseFactory()
            .createGetMapRegionByPositionUseCase(regionsDbApi = mockRegionsDbApi)

        // test
        val mapRegionId = getMapRegionByPositionUseCase(
            position = testData.regionPosition
        )

        // test results
        assertTrue { mapRegionId == "01001" }

    }

}