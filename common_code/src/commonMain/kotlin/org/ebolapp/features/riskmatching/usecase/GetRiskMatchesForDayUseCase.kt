package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch

interface GetRiskMatchesForDayUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): List<RiskMatch>
}

internal class GetRiskMatchesForDayUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi,
    private val performRiskMatchingForDayUseCase:  PerformRiskMatchingForDayUseCase
): GetRiskMatchesForDayUseCase {
    @Throws(Throwable::class)
    override suspend fun invoke(timestampSec: Long): List<RiskMatch> {
        performRiskMatchingForDayUseCase(timestampSec)
        return riskMatchDbApi.loadRiskMatches(timestampSec)
    }
}