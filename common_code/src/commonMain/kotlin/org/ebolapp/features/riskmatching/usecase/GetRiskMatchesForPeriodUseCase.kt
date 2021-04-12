package org.ebolapp.features.riskmatching.usecase
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.utils.DateUtils

interface GetRiskMatchesForPeriodUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(daysPeriod: Int) : List<RiskMatch>
}

class GetRiskMatchesForPeriodUseCaseImpl(
    private val getRiskMatchesForDayUseCase: GetRiskMatchesForDayUseCase
): GetRiskMatchesForPeriodUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(daysPeriod: Int): List<RiskMatch> =
        (daysPeriod downTo 0)
            .map { daysAgo -> DateUtils.timestampAtStartOfDayByAddingDays(days = -daysAgo) }
            .map { timestampDayStartSec -> getRiskMatchesForDayUseCase(timestampDayStartSec) }
            .flatten()
}