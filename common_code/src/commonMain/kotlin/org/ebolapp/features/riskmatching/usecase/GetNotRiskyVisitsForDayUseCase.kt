package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.features.visits.usecases.GetVisitsForDayUseCase

interface GetNotRiskyVisitsForDayUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): List<Visit>
}

internal class GetNotRiskyVisitsForDayUseCaseImpl(
    private val getRiskMatchesForDayUseCase: GetRiskMatchesForDayUseCase,
    private val getVisitsForDayUseCase: GetVisitsForDayUseCase
) : GetNotRiskyVisitsForDayUseCase {

    override suspend fun invoke(timestampSec: Long): List<Visit> {
        val riskMatchesForDay = getRiskMatchesForDayUseCase(timestampSec)
        val visitsForDay = getVisitsForDayUseCase(timestampSec)
        val notRiskyVisitsId = visitsForDay.map { it.id } - riskMatchesForDay.map { it.visitId }
        return visitsForDay.filter { it.id in notRiskyVisitsId }
    }

}
