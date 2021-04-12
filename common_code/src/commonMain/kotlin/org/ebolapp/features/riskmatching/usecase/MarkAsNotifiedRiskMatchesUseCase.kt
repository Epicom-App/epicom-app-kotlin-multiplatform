package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch

interface MarkAsNotifiedRiskMatchesUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(riskMatches: List<RiskMatch>)
}

internal class MarkAsNotifiedRiskMatchesUseCaseImpl(
    private val riskMatchesDbApi: RiskMatchDbApi
) : MarkAsNotifiedRiskMatchesUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(riskMatches: List<RiskMatch>) {
        riskMatchesDbApi.markAsNotifiedRiskMatches(riskMatches)
    }
}
