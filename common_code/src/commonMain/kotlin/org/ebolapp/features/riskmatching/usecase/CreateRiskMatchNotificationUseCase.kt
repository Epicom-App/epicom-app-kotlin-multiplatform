package org.ebolapp.features.riskmatching.usecase

import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch

interface CreateRiskMatchNotificationUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(riskMatch: RiskMatch)
}

class CreateRiskMatchNotificationUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi,
    private val analyseRiskMatchUseCase: AnalyseRiskMatchUseCase
): CreateRiskMatchNotificationUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(riskMatch: RiskMatch) {

        // Do nothing if notification request for the visit already created
        if (riskMatchDbApi.getCountRiskMatchNotifications(riskMatch.visitId) > 0)
            return

        try {
            if (analyseRiskMatchUseCase.invoke(riskMatch) != RiskMatchType.REMAIN)
                riskMatchDbApi.saveOrUpdateRiskMatchNotification(riskMatch)
        } catch (e: Throwable) {
            // This usecase should not interrupt the risk matching in general
            return
        }
    }
}
