package org.ebolapp.features.visits.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.features.visits.test.data.MockVisitsDbApi
import org.ebolapp.features.visits.test.data.UpdateVisitsTestData
import org.ebolapp.features.visits.usecases.UpdateVisitsUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class UpdateVisitsTest {
    

    @Test
    fun update_visits_use_case_last_visit_is_null_test() = runBlocking {

        val testData = UpdateVisitsTestData.LastVisitIsNull

        val mockVisitsDbApi = object: MockVisitsDbApi() {
            val visits = mutableListOf<Visit>()
            override suspend fun getLastVisit(): Visit? = null
            override suspend fun insertVisit(visit: Visit) {
               visits.add(visit.copy(id = visits.size.toLong()))
            }
        }
        
        // Test subject
        val updateVisitsUseCase = UpdateVisitsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        updateVisitsUseCase(testData.userLocation)

        assertTrue { mockVisitsDbApi.visits.size == 1 }
        assertTrue { mockVisitsDbApi.visits.first() == testData.expectVisit }

    }

    @Test
    fun update_visits_use_case_last_visit_in_range_of_new_user_location_test() = runBlocking {

        val testData = UpdateVisitsTestData.LastVisitInRangeOfNewUserLocation

        val mockVisitsDbApi = object: MockVisitsDbApi() {
            val visits = mutableListOf(testData.lastVisit)
            override suspend fun getLastVisit(): Visit? {
                return visits.maxByOrNull { it.endTimestamp }
            }
            override suspend fun updateVisit(visit: Visit) {
                val v = visits.find { it.id == visit.id  }
                visits.remove(v)
                visits.add(visit)
            }
        }

        // Test subject
        val updateVisitsUseCase =  UpdateVisitsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        updateVisitsUseCase(testData.newUserLocation)

        assertTrue { mockVisitsDbApi.visits.size == 1 }
        assertTrue { mockVisitsDbApi.visits.first() == testData.expectUpdatedVisit }

    }

    @Test
    fun update_visits_use_case_last_visit_not_in_range_of_new_user_location_test() = runBlocking {

        val testData = UpdateVisitsTestData.LastVisitNotInRangeOfNewUserLocation

        val mockVisitsDbApi = object: MockVisitsDbApi() {
            val visits = mutableListOf(testData.lastVisit)
            override suspend fun getLastVisit(): Visit? {
                return visits.maxByOrNull { it.endTimestamp }
            }
            override suspend fun insertVisit(visit: Visit) {
                visits.add(visit.copy(id = visits.size.toLong()))
            }
        }

        // Test subject
        val updateVisitsUseCase = UpdateVisitsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        updateVisitsUseCase(testData.newUserLocation)

        assertTrue { mockVisitsDbApi.visits.size == 2 }
        assertTrue { mockVisitsDbApi.visits.last() == testData.expectUpdatedVisit }
    }

}