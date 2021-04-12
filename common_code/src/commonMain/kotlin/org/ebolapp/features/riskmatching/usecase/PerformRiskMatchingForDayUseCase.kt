package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.regions.usecases.GetMapRegionIdByPositionUseCase
import org.ebolapp.features.regions.usecases.GetMapRegionInfoUseCase
import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.visits.usecases.GetVisitsBetweenTimestampsUseCase
import org.ebolapp.utils.DateUtils

/**
 * Performs risk matching calculations for the given day
 * Returns true if new risk matches were discovered
 * Expected to be called multiple times during the day
 */
interface PerformRiskMatchingForDayUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): Boolean
}

class PerformRiskMatchingForDayUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi,
    private val getVisitsBetweenTimestampsUseCase: GetVisitsBetweenTimestampsUseCase,
    private val getMapRegionIdByPositionUseCase: GetMapRegionIdByPositionUseCase,
    private val getMapRegionInfoUseCase: GetMapRegionInfoUseCase,
    private val cleanRiskMatchesCacheUseCase: CleanRiskMatchesCacheUseCase,
    private val createRiskMatchNotificationUseCase: CreateRiskMatchNotificationUseCase
) : PerformRiskMatchingForDayUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(timestampSec: Long): Boolean {

        // Clean cache at first
        cleanRiskMatchesCacheUseCase()

        // Get timestamps of the day start and end
        val dayStartTimeStamp = DateUtils.dayStartTimestamp(timestampSec)
        val dayEndTimeStamp = DateUtils.dayEndTimestamp(timestampSec)

        var hasNewMatches = false

        // Get all visits included to the given period
        val visitsInInterval = getVisitsBetweenTimestampsUseCase(
            startTimestampSec = dayStartTimeStamp,
            endTimestampSec = dayEndTimeStamp
        )

        // For each visit perform risk matching
        visitsInInterval.forEach visit@{ visit ->
            val regionId = getMapRegionIdByPositionUseCase(visit.position) ?: return@visit
            val regionInfo = getMapRegionInfoUseCase(regionId, dayStartTimeStamp) ?: return@visit

            // If region is risky save or update current risk match db record
            if (regionInfo.isRisky) {
                RiskMatch(regionInfo, visit, dayStartTimeStamp).let { riskMatch ->
                    riskMatchDbApi.saveOrUpdateRiskMatch(riskMatch)
                    createRiskMatchNotificationUseCase(riskMatch)
                    hasNewMatches = true
                }
            }
        }

        return hasNewMatches
    }
}
