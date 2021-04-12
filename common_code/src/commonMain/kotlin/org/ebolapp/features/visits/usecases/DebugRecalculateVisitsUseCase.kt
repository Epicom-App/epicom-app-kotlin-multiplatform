package org.ebolapp.features.visits.usecases

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.usecases.locations.GetUserLocationsForDayByTimestampUseCase
import org.ebolapp.utils.DateUtils

/*
 * This is a DEBUG use case which
 * - removes all visits data
 * - removes all risk matching data
 * - recalculates all visits based on user locations
 * After running of this use case all risk matches will be recalculated.
 */

interface DebugRecalculateVisitsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke()
}

class DebugRecalculateVisitsUseCaseImpl(
    private val getUserLocationsForDayByTimestampUseCase: GetUserLocationsForDayByTimestampUseCase,
    private val updateVisitsUseCase: UpdateVisitsUseCase,
    private val riskMatchDbApi: RiskMatchDbApi,
    private val visitsDbApi: VisitsDbApi
): DebugRecalculateVisitsUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke() {

        // Delete all cache
        val deleteBeforeTimestamp = DateUtils.dayEndTimestamp(DateUtils.nowTimestampSec())
        riskMatchDbApi.deleteRiskMatchesBefore(deleteBeforeTimestamp)
        visitsDbApi.deleteVisitsBeforeTimestamp(deleteBeforeTimestamp)

        // Get all related locations
        val allLocations = (16 downTo 0)
            .map { offset -> DateUtils.timestampAtStartOfDayByAddingDays(days = -offset) }
            .map { timestampSec -> getUserLocationsForDayByTimestampUseCase(timestampSec) }
            .flatten()
            .sortedBy { it.timestampSec }

        // Recalculate visits
        allLocations.forEach { updateVisitsUseCase.invoke(it) }
    }
}