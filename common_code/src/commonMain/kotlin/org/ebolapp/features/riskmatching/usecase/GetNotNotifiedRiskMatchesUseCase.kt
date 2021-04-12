package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.NotNotifiedRiskMatch
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.utils.DateUtils

interface GetNotNotifiedRiskMatchesUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(
        upToTimestampSec: Long = DateUtils.nowTimestampSec()
    ): List<NotNotifiedRiskMatch>
}

internal class GetNotNotifiedRiskMatchesUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi,
    private val performRiskMatchingForDayUseCase: PerformRiskMatchingForDayUseCase,
    private val analyseRiskMatchUseCase: AnalyseRiskMatchUseCase
) : GetNotNotifiedRiskMatchesUseCase {
    @Throws(Throwable::class)
    override suspend fun invoke(upToTimestampSec: Long): List<NotNotifiedRiskMatch> {

        performRiskMatchingForDayUseCase(upToTimestampSec)

        val unNotifiedRiskMatches = riskMatchDbApi
            .loadUnNotifiedRiskMatches()
            .sortedBy { it.dayStartTimestampSec }

        return unNotifiedRiskMatches.map {
            NotNotifiedRiskMatch(
                riskMatch = it,
                type = analyseRiskMatchUseCase(it)
            )
        }
    }
}

