package org.ebolapp.features.visits.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.features.visits.test.data.GetVisitsBetweenTimestampTestData
import org.ebolapp.features.visits.test.data.MockVisitsDbApi
import org.ebolapp.features.visits.usecases.GetVisitsBetweenTimestampsUseCaseImpl
import kotlin.test.Test
import kotlin.test.assertTrue

class GetVisitsBetweenTimestampUseCaseTest {

    // Like the db query
    private val dbTestQuery = fun(visit: Visit, start: Long, end: Long): Boolean {
        return (visit.startTimestamp < start && visit.endTimestamp >= start) || // starts before and ends inside period
                (visit.startTimestamp >= start && visit.endTimestamp <= end) || // starts and ends inside period
                (visit.startTimestamp <= end && visit.endTimestamp > end) || // starts inside and ends after period
                (visit.startTimestamp < start && visit.endTimestamp > end) // starts before and ends after period
    }

    @Test
    fun get_visits_between_timestamp_one_visit_starts_before_requested_interval() = runBlocking {
        val testData = GetVisitsBetweenTimestampTestData.OneVisitStartsBeforeRequestedInterval
        val mockVisitsDbApi = object : MockVisitsDbApi() {
            val visits = mutableListOf(testData.visitOne, testData.visitTwo, testData.visitThree)
            override suspend fun getVisitsBetween(
                startTimestampSec: Long,
                endTimestampSec: Long,
                minVisitDurationSec: Long
            ): List<Visit> {
                return visits.filter { visit ->
                    dbTestQuery(
                        visit,
                        startTimestampSec,
                        endTimestampSec
                    )
                }
            }
        }

        val getVisitsBetweenTimestampUseCase = GetVisitsBetweenTimestampsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        val visitsBetweenTimestamps = getVisitsBetweenTimestampUseCase(
            startTimestampSec = testData.startDayTimestamp,
            endTimestampSec = testData.endDayTimestamp
        )

        assertTrue { visitsBetweenTimestamps.size == 3 }
        assertTrue { visitsBetweenTimestamps[0] == testData.visitOne }
        assertTrue { visitsBetweenTimestamps[1] == testData.visitTwo }
        assertTrue { visitsBetweenTimestamps[2] == testData.visitThree }
    }


    @Test
    fun get_visits_between_timestamp_all_visits_same_are_inside_the_interval_test() = runBlocking {
        val testData = GetVisitsBetweenTimestampTestData.AllVisitsAreInsideTheInterval
        val mockVisitsDbApi = object : MockVisitsDbApi() {
            val visits = mutableListOf(testData.visitOne, testData.visitTwo, testData.visitThree)
            override suspend fun getVisitsBetween(
                startTimestampSec: Long,
                endTimestampSec: Long,
                minVisitDurationSec: Long
            ): List<Visit> {
                // Like the db query
                return visits.filter { visit ->
                    dbTestQuery(
                        visit,
                        startTimestampSec,
                        endTimestampSec
                    )
                }
            }
        }

        val getVisitsBetweenTimestampUseCase = GetVisitsBetweenTimestampsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        val visitsBetweenTimestamps = getVisitsBetweenTimestampUseCase(
            startTimestampSec = testData.startDayTimestamp,
            endTimestampSec = testData.endDayTimestamp
        )

        assertTrue { visitsBetweenTimestamps.size == 3 }
        assertTrue { visitsBetweenTimestamps[0] == testData.visitOne }
        assertTrue { visitsBetweenTimestamps[1] == testData.visitTwo }
        assertTrue { visitsBetweenTimestamps[2] == testData.visitThree }
    }

    @Test
    fun get_visits_between_timestamp_one_visit_ends_after_interval_test() = runBlocking {
        val testData = GetVisitsBetweenTimestampTestData.OneVisitEndsAfterRequestedInterval
        val mockVisitsDbApi = object : MockVisitsDbApi() {
            val visits = mutableListOf(testData.visitOne, testData.visitTwo, testData.visitThree)
            override suspend fun getVisitsBetween(
                startTimestampSec: Long,
                endTimestampSec: Long,
                minVisitDurationSec: Long
            ): List<Visit> {
                // Like the db query
                return visits.filter { visit ->
                    dbTestQuery(
                        visit,
                        startTimestampSec,
                        endTimestampSec
                    )
                }
            }
        }

        val getVisitsBetweenTimestampUseCase = GetVisitsBetweenTimestampsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        val visitsBetweenTimestamps = getVisitsBetweenTimestampUseCase(
            startTimestampSec = testData.startDayTimestamp,
            endTimestampSec = testData.endDayTimestamp
        )

        assertTrue { visitsBetweenTimestamps.size == 3 }
        assertTrue { visitsBetweenTimestamps[0] == testData.visitOne }
        assertTrue { visitsBetweenTimestamps[1] == testData.visitTwo }
        assertTrue { visitsBetweenTimestamps[2] == testData.visitThree }
    }

    @Test
    fun get_visits_between_timestamp_visit_starts_before_and_ends_after_interval_test() = runBlocking {
        val testData = GetVisitsBetweenTimestampTestData.VisitStartsBeforeAndEndsAfterInterval
        val mockVisitsDbApi = object : MockVisitsDbApi() {
            val visits = mutableListOf(testData.visitOne)
            override suspend fun getVisitsBetween(
                startTimestampSec: Long,
                endTimestampSec: Long,
                minVisitDurationSec: Long
            ): List<Visit> {
                // Like the db query
                return visits.filter { visit ->
                    dbTestQuery(
                        visit,
                        startTimestampSec,
                        endTimestampSec
                    )
                }
            }
        }

        val getVisitsBetweenTimestampUseCase = GetVisitsBetweenTimestampsUseCaseImpl(
            visitsDbApi = mockVisitsDbApi
        )

        val visitsBetweenTimestamps = getVisitsBetweenTimestampUseCase(
            startTimestampSec = testData.startDayTimestamp,
            endTimestampSec = testData.endDayTimestamp
        )

        assertTrue { visitsBetweenTimestamps.size == 1 }
        assertTrue { visitsBetweenTimestamps[0] == testData.visitOne }
    }
}