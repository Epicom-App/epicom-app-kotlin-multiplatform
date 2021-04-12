package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.visits.db.VisitsDbApi

/**
 * RiskMatchType means that new risk match was detected when:
 * REGION_CHANGE -> user changed region
 * SEVERITY_INCREASE -> the region is the same, but severity level increased
 * REMAIN -> user remains in the same region but changed location
 */
enum class RiskMatchType {
    REGION_CHANGE, SEVERITY_INCREASE, REMAIN
}

interface AnalyseRiskMatchUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(riskMatch: RiskMatch) : RiskMatchType
}

class AnalyseRiskMatchUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi,
    private val visitsDbApi: VisitsDbApi
) : AnalyseRiskMatchUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(riskMatch: RiskMatch): RiskMatchType {
        val prevVisit = visitsDbApi.getVisitBeforeVisitWithId(riskMatch.visitId - 1)
        val prevVisitRiskMatch = prevVisit?.let { riskMatchDbApi.getRiskMatchForVisit(it.id) }
        return prevVisitRiskMatch?.let {
            return when {
                it.regionId != riskMatch.regionId -> {
                    RiskMatchType.REGION_CHANGE
                }
                it.severity < riskMatch.severity -> {
                    RiskMatchType.SEVERITY_INCREASE
                }
                else -> {
                    RiskMatchType.REMAIN
                }
            }
        } ?: RiskMatchType.REGION_CHANGE
    }
}