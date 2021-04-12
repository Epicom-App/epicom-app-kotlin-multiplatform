package org.ebolapp.features.riskmatching.test

import kotlinx.coroutines.runBlocking
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.regions.usecases.GetMapRegionIdByPositionUseCase
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCase
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.riskmatching.test.data.MockRiskMatchingDbApi
import org.ebolapp.features.riskmatching.test.data.PerformRiskMatchingTestsData
import org.ebolapp.features.riskmatching.usecase.CleanRiskMatchesCacheUseCase
import org.ebolapp.features.riskmatching.usecase.CreateRiskMatchNotificationUseCase
import org.ebolapp.features.riskmatching.usecase.PerformRiskMatchingForDayUseCase
import org.ebolapp.features.riskmatching.usecase.PerformRiskMatchingForDayUseCaseImpl
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.features.visits.usecases.GetVisitsBetweenTimestampsUseCase
import org.ebolapp.utils.DateUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class PerformRiskMatchingTests {

    @Test
    fun perform_risk_matching_test_positive() = runBlocking {
        // test data
        val testData = PerformRiskMatchingTestsData.PerformRiskMatchingPositive
        val riskMatchingDbApi = MockRiskMatchingDbApi()

        // test subject
        val performRiskMatchingForDayUseCase: PerformRiskMatchingForDayUseCase =
            PerformRiskMatchingForDayUseCaseImpl(
                riskMatchDbApi = riskMatchingDbApi,
                getVisitsBetweenTimestampsUseCase = object : GetVisitsBetweenTimestampsUseCase {
                    override suspend fun invoke(
                        startTimestampSec: Long,
                        endTimestampSec: Long,
                        minimalVisitDuration: Long
                    ): List<Visit> = testData.visits.filter { it.durationSec() >= Visit.MIN_VISIT_DURATION_SEC  }
                },
                getMapRegionIdByPositionUseCase = object : GetMapRegionIdByPositionUseCase {
                    override suspend fun invoke(
                        position: Position
                    ): String? = testData.mapRegionId[position]
                },
                getMapRegionInfoUseCase = object : GetMapRegionInfoUseCase {
                    override suspend fun invoke(
                        regionId: String,
                        timestampSec: Long
                    ): MapRegionInfo? {
                        return testData.mapRegionInfo[regionId]
                    }
                },
                cleanRiskMatchesCacheUseCase = object : CleanRiskMatchesCacheUseCase {
                    override suspend fun invoke() {}
                },
                createRiskMatchNotificationUseCase = object : CreateRiskMatchNotificationUseCase {
                    override suspend fun invoke(riskMatch: RiskMatch) {}
                }
            )

        // perform risk matching, always run for multiple days
        // it is expected that it will be called many times in cycle
        // for 14 days starting from current day
        (0..5).forEach { offset ->
            val timestamp = DateUtils.timestampAtStartOfDayByAddingDays(testData.nowRiskMatchTimestamp, days = -offset)
            performRiskMatchingForDayUseCase(timestamp)
        }

        // test results

        assertTrue { riskMatchingDbApi.riskMatchSavedList.size == 2 }
        assertTrue { riskMatchingDbApi.riskMatchSavedList.map { it.regionId }.contains("regionId_1") }
        assertTrue { riskMatchingDbApi.riskMatchSavedList.map { it.regionId }.contains("regionId_2") }

    }

    @Test
    fun perform_risk_matching_test_one_visit_is_cross_3_days() = runBlocking {

        // test data
        val testData = PerformRiskMatchingTestsData.PerformRiskMatchingPositiveCross3Days
        val riskMatchingDbApi = MockRiskMatchingDbApi()

        val getVisitsBetweenTimestampsDbLikeQuery = fun(visit: Visit, start: Long, end: Long): Boolean {
            return (visit.startTimestamp < start && visit.endTimestamp >= start) || // starts before and ends inside period
                (visit.startTimestamp >= start && visit.endTimestamp <= end) || // starts and ends inside period
                (visit.startTimestamp <= end && visit.endTimestamp > end) || // starts inside and ends after period
                (visit.startTimestamp < start && visit.endTimestamp > end) // starts before and ends after period
        }

        // test subject
        val performRiskMatchingForDayUseCase: PerformRiskMatchingForDayUseCase =
            PerformRiskMatchingForDayUseCaseImpl(
                riskMatchDbApi = riskMatchingDbApi,
                getVisitsBetweenTimestampsUseCase = object : GetVisitsBetweenTimestampsUseCase {
                    override suspend fun invoke(
                        startTimestampSec: Long,
                        endTimestampSec: Long,
                        minimalVisitDuration: Long
                    ): List<Visit> = testData.visits
                        .filter { getVisitsBetweenTimestampsDbLikeQuery(it,startTimestampSec,endTimestampSec)}
                        .filter { it.durationSec() >= Visit.MIN_VISIT_DURATION_SEC  }
                },
                getMapRegionIdByPositionUseCase = object : GetMapRegionIdByPositionUseCase {
                    override suspend fun invoke(
                        position: Position
                    ): String? = testData.mapRegionId[position]
                },
                getMapRegionInfoUseCase = object : GetMapRegionInfoUseCase {
                    override suspend fun invoke(
                        regionId: String,
                        timestampSec: Long
                    ): MapRegionInfo? {
                        return testData.mapRegionInfo[regionId]
                    }
                },
                cleanRiskMatchesCacheUseCase = object : CleanRiskMatchesCacheUseCase {
                    override suspend fun invoke() {}
                },
                createRiskMatchNotificationUseCase = object : CreateRiskMatchNotificationUseCase {
                    override suspend fun invoke(riskMatch: RiskMatch) {}
                }
            )

        // perform risk matching, always run for multiple days
        // it is expected that it will be called many times in cycle
        // for 14 days starting from current day
        (0..5).forEach { offset ->
            val timestamp = DateUtils.timestampAtStartOfDayByAddingDays(testData.nowRiskMatchTimestamp, days = -offset)
            performRiskMatchingForDayUseCase(timestamp)
        }

        // test results

        assertTrue { riskMatchingDbApi.riskMatchSavedList.size == 1 }
        assertTrue { riskMatchingDbApi.riskMatchSavedList.first().regionId == "regionId_1" }
    }

    @Test
    fun perform_risk_matching_test_multiple_visits_across_3_days() = runBlocking {

        // test data
        val testData = PerformRiskMatchingTestsData.PerformRiskMatchingMultipleVisitsAcross3Days
        val riskMatchingDbApi = MockRiskMatchingDbApi()

        val getVisitsBetweenTimestampsDbLikeQuery = fun(visit: Visit, start: Long, end: Long): Boolean {
            return (visit.startTimestamp < start && visit.endTimestamp >= start) || // starts before and ends inside period
                (visit.startTimestamp >= start && visit.endTimestamp <= end) || // starts and ends inside period
                (visit.startTimestamp <= end && visit.endTimestamp > end) || // starts inside and ends after period
                (visit.startTimestamp < start && visit.endTimestamp > end) // starts before and ends after period
        }

        // test subject
        val performRiskMatchingForDayUseCase: PerformRiskMatchingForDayUseCase =
            PerformRiskMatchingForDayUseCaseImpl(
                riskMatchDbApi = riskMatchingDbApi,
                getVisitsBetweenTimestampsUseCase = object : GetVisitsBetweenTimestampsUseCase {
                    override suspend fun invoke(
                        startTimestampSec: Long,
                        endTimestampSec: Long,
                        minimalVisitDuration: Long
                    ): List<Visit> = testData.visits
                        .filter { getVisitsBetweenTimestampsDbLikeQuery(it,startTimestampSec,endTimestampSec)}
                        .filter { it.durationSec() >= Visit.MIN_VISIT_DURATION_SEC  }
                },
                getMapRegionIdByPositionUseCase = object : GetMapRegionIdByPositionUseCase {
                    override suspend fun invoke(
                        position: Position
                    ): String? = testData.mapRegionId[position]
                },
                getMapRegionInfoUseCase = object : GetMapRegionInfoUseCase {
                    override suspend fun invoke(
                        regionId: String,
                        timestampSec: Long
                    ): MapRegionInfo? {
                        return testData.mapRegionInfo[regionId]
                    }
                },
                cleanRiskMatchesCacheUseCase = object : CleanRiskMatchesCacheUseCase {
                    override suspend fun invoke() {}
                },
                createRiskMatchNotificationUseCase = object : CreateRiskMatchNotificationUseCase {
                    override suspend fun invoke(riskMatch: RiskMatch) {}
                }
            )

        // perform risk matching, always run for multiple days
        // it is expected that it will be called many times in cycle
        // for 14 days starting from current day
        (0..5).forEach { offset ->
            val timestamp = DateUtils.timestampAtStartOfDayByAddingDays(testData.nowRiskMatchTimestamp, days = -offset)
            performRiskMatchingForDayUseCase(timestamp)
        }

        // test results

        assertTrue { riskMatchingDbApi.riskMatchSavedList.size == 4 }
    }

}