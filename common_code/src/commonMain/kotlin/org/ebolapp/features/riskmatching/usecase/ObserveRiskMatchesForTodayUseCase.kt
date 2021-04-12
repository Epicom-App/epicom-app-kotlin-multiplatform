package org.ebolapp.features.riskmatching.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.visits.usecases.ObserveVisitsUseCase
import org.ebolapp.utils.DateUtils


interface ObserveRiskMatchesForTodayUseCase {
    operator fun invoke(): Flow<List<RiskMatch>>
}

internal class ObserveRiskMatchesForTodayUseCaseImpl(
    private val observeVisitsUseCase: ObserveVisitsUseCase,
    private val performRiskMatchingForDayUseCase: PerformRiskMatchingForDayUseCase,
    private val getRiskMatchesForDayUseCase: GetRiskMatchesForDayUseCase
): ObserveRiskMatchesForTodayUseCase {

    override fun invoke(): Flow<List<RiskMatch>> = observeVisitsUseCase().map {
        val timestampSec = DateUtils.dayStartTimestamp(DateUtils.nowTimestampSec())
        performRiskMatchingForDayUseCase(timestampSec)
        getRiskMatchesForDayUseCase(timestampSec)
    }
}