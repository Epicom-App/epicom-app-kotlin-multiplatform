package org.ebolapp.features.visits.test

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.features.visits.test.data.MockVisitsDbApi
import org.ebolapp.features.visits.test.data.ObserveVisitsTestData
import org.ebolapp.features.visits.usecases.ObserveVisitsUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class ObserveVisitsUseCaseTest {

    @Test
    fun observe_visits_basic_test() = runBlocking {
        val testData = ObserveVisitsTestData.Basic
        var emissionsCount = 0
        val mockVisitsDbApi = object: MockVisitsDbApi() {
            override fun observeVisits(
                timestampSecDayStart: Long,
                minVisitDurationSec: Long
            ): Flow<List<Visit>> = flow {
                emissionsCount = 1
                emit(testData.firstVisitsBatch)
                delay(300)
                emissionsCount = 2
                emit(
                    listOf(
                        testData.firstVisitsBatch,
                        testData.secondVisitsBatch
                    ).flatten()
                )
                delay(300)
                emissionsCount = 3
                emit(
                    listOf(
                        testData.firstVisitsBatch,
                        testData.secondVisitsBatch,
                        testData.thirdVisitsBatch
                    ).flatten()
                )
            }
        }

        val observeVisitsUseCase = ObserveVisitsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        observeVisitsUseCase().collect { visits ->
            when (emissionsCount) {
                1 -> {
                    assertTrue { visits.size == 3 }
                }
                2 -> {
                    assertTrue { visits.size == 6 }
                }
                3 -> {
                    assertTrue { visits.size == 9 }
                }
                else -> {
                    assertTrue { false }
                }
            }
        }
    }
}