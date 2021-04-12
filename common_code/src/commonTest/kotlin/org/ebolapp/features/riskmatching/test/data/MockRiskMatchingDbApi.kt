package org.ebolapp.features.riskmatching.test.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.utils.DateUtils

open class MockRiskMatchingDbApi : RiskMatchDbApi {

    val riskMatchSavedList = mutableListOf<RiskMatch>()

    override suspend fun saveOrUpdateRiskMatch(riskMatch: RiskMatch) {
        if (!riskMatchSavedList.map { it.visitId }.contains(riskMatch.visitId)) {
            riskMatchSavedList.add(riskMatch)
        }
    }

    override suspend fun saveOrUpdateRiskMatchNotification(riskMatch: RiskMatch) {
        // TODO: Not yet implemented
    }

    override suspend fun getRiskMatchForVisit(visitId: Long): RiskMatch? {
        return null
    }

    override suspend fun getCountRiskMatchNotifications(visitId: Long): Long {
        return 0
    }

    override suspend fun loadRiskMatches(timestampSec: Long): List<RiskMatch> {
        val dateStart = DateUtils.dayStartTimestamp(timestampSec)
        return riskMatchSavedList.filter { it.dayStartTimestampSec == dateStart }
    }

    override suspend fun deleteRiskMatchesBefore(timestampSec: Long) {
        riskMatchSavedList.removeAll { it.dayStartTimestampSec < timestampSec }
    }

    override suspend fun deleteRiskMatchesNotificationsBefore(timestampSec: Long) {
        // TODO: Not yet implemented
    }

    override suspend fun loadUnNotifiedRiskMatches(): List<RiskMatch> {
        // TODO Not yet implemented
        return emptyList()
    }

    override fun observeUnNotifiedRiskMatches(): Flow<List<RiskMatch>> {
        // TODO Not yet implemented
        return flowOf(emptyList())
    }

    override suspend fun markAsNotifiedRiskMatches(riskMatches: List<RiskMatch>) {
        // TODO Not yet implemented
    }
}