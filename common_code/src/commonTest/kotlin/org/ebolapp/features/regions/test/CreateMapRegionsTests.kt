package org.ebolapp.features.regions.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.features.regions.api.DistrictRegion
import org.ebolapp.features.regions.api.StateRegion
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.test.data.CreateMapRegionsTestsData
import org.ebolapp.features.regions.test.data.MapRegionsTestUseCaseFactory
import org.ebolapp.features.regions.test.data.MockRegionsDbApi
import kotlin.test.Test
import kotlin.test.assertTrue

class CreateMapRegionsTests {
    
    @Test
    fun create_map_regions_use_case_test_empty_db_regions() = runBlocking {
        // test data
        val testData = CreateMapRegionsTestsData.CreateMapRegionsUseCaseTestEmptyDbRegions
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            var deleted = false
            var stateRegion : Map<StateRegion, MapRegionBox> = mutableMapOf()
            var districtRegion: Map<DistrictRegion, MapRegionBox> = mutableMapOf()
            override suspend fun deleteMapRegions() { deleted = true }
            override suspend fun countChildMapRegions() = 0L
            override suspend fun countParentMapRegions() = 0L
            override suspend fun insertParentMapRegion(
                parentMapRegions: Map<StateRegion, MapRegionBox>
            ) {
                stateRegion = stateRegion + parentMapRegions
            }
            override suspend fun insertChildMapRegion(
                childMapRegions: Map<DistrictRegion, MapRegionBox>
            ) {
                districtRegion = districtRegion + childMapRegions
            }
        }

        // test subject
        val createMapRegionsUseCase = MapRegionsTestUseCaseFactory()
            .createCreateMapRegionsUseCase(
                regionsDbApi = mockRegionsDbApi,
                testData.mockStatesFileReader,
                testData.mockDistrictsFileReader
            )

        // test
        createMapRegionsUseCase()

        // test results
        assertTrue { mockRegionsDbApi.deleted }
        assertTrue { mockRegionsDbApi.stateRegion.keys.size == 2 }
        assertTrue { mockRegionsDbApi.stateRegion.keys.find { it.id == "1" }?.id == "1" }
        assertTrue { mockRegionsDbApi.stateRegion.keys.find { it.id == "2" }?.id == "2" }
        assertTrue { mockRegionsDbApi.districtRegion.keys.size == 2 }
        assertTrue { mockRegionsDbApi.districtRegion.keys.find { it.id == "01001" }?.id == "01001" }
        assertTrue { mockRegionsDbApi.districtRegion.keys.find { it.id == "01002" }?.id == "01002" }

    }

    @Test
    fun create_map_regions_use_case_test_already_populated_db_regions() = runBlocking {
        // test data
        val testData = CreateMapRegionsTestsData.CreateMapRegionsUseCaseTestAlreadyPopulatedDbRegions
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            override suspend fun countChildMapRegions() = 2L
            override suspend fun countParentMapRegions() = 2L
        }

        // test subject
        val createMapRegionsUseCase = MapRegionsTestUseCaseFactory()
            .createCreateMapRegionsUseCase(
                regionsDbApi = mockRegionsDbApi,
                testData.mockStatesFileReader,
                testData.mockDistrictsFileReader
            )

        // test
        createMapRegionsUseCase()

        // test results
        assertTrue { true } // we reached here the test passed

    }

    @Test
    fun create_map_regions_use_case_test_half_populated_db_regions() = runBlocking {
        // test data
        val testData = CreateMapRegionsTestsData.CreateMapRegionsUseCaseTestHalfPopulatedDbRegions
        val mockRegionsDbApi = object : MockRegionsDbApi() {
            var deleted = false
            var stateRegion : Map<StateRegion, MapRegionBox> = mutableMapOf()
            var districtRegion: Map<DistrictRegion, MapRegionBox> = mutableMapOf()
            override suspend fun deleteMapRegions() { deleted = true }
            override suspend fun countChildMapRegions() = 0L // not populated
            override suspend fun countParentMapRegions() = 2L // populated
            override suspend fun insertParentMapRegion(
                parentMapRegions: Map<StateRegion, MapRegionBox>
            ) {
                stateRegion = stateRegion + parentMapRegions
            }
            override suspend fun insertChildMapRegion(
                childMapRegions: Map<DistrictRegion, MapRegionBox>
            ) {
                districtRegion = districtRegion + childMapRegions
            }
        }

        // test subject
        val createMapRegionsUseCase = MapRegionsTestUseCaseFactory()
            .createCreateMapRegionsUseCase(
                regionsDbApi = mockRegionsDbApi,
                testData.mockStatesFileReader,
                testData.mockDistrictsFileReader
            )

        // test
        createMapRegionsUseCase()

        // test results
        assertTrue { mockRegionsDbApi.deleted }
        assertTrue { mockRegionsDbApi.stateRegion.keys.size == 2 }
        assertTrue { mockRegionsDbApi.stateRegion.keys.find { it.id == "1" }?.id == "1" }
        assertTrue { mockRegionsDbApi.stateRegion.keys.find { it.id == "2" }?.id == "2" }
        assertTrue { mockRegionsDbApi.districtRegion.keys.size == 2 }
        assertTrue { mockRegionsDbApi.districtRegion.keys.find { it.id == "01001" }?.id == "01001" }
        assertTrue { mockRegionsDbApi.districtRegion.keys.find { it.id == "01002" }?.id == "01002" }

    }

}