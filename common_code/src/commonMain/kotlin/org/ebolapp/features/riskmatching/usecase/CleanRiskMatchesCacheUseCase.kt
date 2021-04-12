package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.utils.Constants
import org.ebolapp.utils.DateUtils

interface CleanRiskMatchesCacheUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke()
}

class CleanRiskMatchesCacheUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi
): CleanRiskMatchesCacheUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke() {
        // Keep matches data the same time as risk cases
        val deleteBeforeTimestamp = DateUtils.timestampAtStartOfDayByAddingDays(days = -Constants.Cache.CASES_CACHE_DAYS_COUNT)
        riskMatchDbApi.deleteRiskMatchesBefore(deleteBeforeTimestamp)
        riskMatchDbApi.deleteRiskMatchesNotificationsBefore(deleteBeforeTimestamp)
    }
}