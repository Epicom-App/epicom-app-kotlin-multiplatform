package org.ebolapp.features.riskmatching.db


import com.squareup.sqldelight.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.ebolapp.db.RiskMatchNotificationTable
import org.ebolapp.db.RiskMatchTable
import org.ebolapp.db.VisitTable
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.utils.DateUtils

interface RiskMatchDbApi {

    suspend fun saveOrUpdateRiskMatch(riskMatch: RiskMatch)
    suspend fun saveOrUpdateRiskMatchNotification(riskMatch: RiskMatch)
    suspend fun loadRiskMatches(timestampSec: Long): List<RiskMatch>
    suspend fun deleteRiskMatchesBefore(timestampSec: Long)
    suspend fun deleteRiskMatchesNotificationsBefore(timestampSec: Long)
    suspend fun getRiskMatchForVisit(visitId: Long) : RiskMatch?
    suspend fun getCountRiskMatchNotifications(visitId: Long) : Long

    suspend fun loadUnNotifiedRiskMatches(): List<RiskMatch>
    fun observeUnNotifiedRiskMatches(): Flow<List<RiskMatch>>
    suspend fun markAsNotifiedRiskMatches(riskMatches: List<RiskMatch>)

}

internal class RiskMatchDbApiImpl(
    databaseWrapper: DatabaseWrapper
) : RiskMatchDbApi {

    private val database = databaseWrapper.database

    private val riskMatchQueries = database.riskMatchTableQueries
    private val riskMatchNotificationQueries = database.riskMatchNotificationTableQueries

    override suspend fun saveOrUpdateRiskMatch(riskMatch: RiskMatch) {
        riskMatchQueries.transaction {
            riskMatchQueries.insertOrReplace(riskMatch.toRiskMatchTable())
        }
    }

    override suspend fun getRiskMatchForVisit(visitId: Long): RiskMatch? =
        riskMatchQueries.selectRiskMatchForVisit(visitId).executeAsOneOrNull()?.toRiskMatch()

    override suspend fun saveOrUpdateRiskMatchNotification(riskMatch: RiskMatch) {
        riskMatchNotificationQueries.transaction {
            riskMatchNotificationQueries.insertOrReplaceUnhandled(
                regionId = riskMatch.regionId,
                visitId = riskMatch.visitId,
                dayStartTimestampSec = riskMatch.dayStartTimestampSec
            )
        }
    }

    override suspend fun getCountRiskMatchNotifications(visitId: Long): Long =
        riskMatchNotificationQueries.selectCountOfRecordsWithVisitId(visitId).executeAsOneOrNull() ?: 0

    override suspend fun loadRiskMatches(timestampSec: Long): List<RiskMatch> =
        riskMatchQueries
            .selectForTimestamp(DateUtils.dayStartTimestamp(timestampSec))
            .executeAsList()
            .map { riskMatchTable -> riskMatchTable.toRiskMatch() }

    override suspend fun deleteRiskMatchesBefore(timestampSec: Long) {
        riskMatchQueries.transaction {
            riskMatchQueries.deleteAllBeforeTimestamp(timestampSec)
        }
    }

    override suspend fun deleteRiskMatchesNotificationsBefore(timestampSec: Long) {
        riskMatchNotificationQueries.transaction {
            riskMatchNotificationQueries.deleteAllBeforeTimestamp(timestampSec)
        }
    }

    override suspend fun loadUnNotifiedRiskMatches(): List<RiskMatch> =
        riskMatchNotificationQueries
            .selectAllUnhandled()
            .executeAsList()
            .map { it.toRiskMatch() }

    override fun observeUnNotifiedRiskMatches(): Flow<List<RiskMatch>> = callbackFlow {
        val unNotifiedRiskMatchesQuery = riskMatchNotificationQueries.selectAllUnhandled()
        
        val unNotifiedRiskMatchesListener = object : Query.Listener {
            override fun queryResultsChanged() {
                this@callbackFlow.offer(queryNotNotifiedRiskMatches(unNotifiedRiskMatchesQuery))
            }
        }
        offer(queryNotNotifiedRiskMatches(unNotifiedRiskMatchesQuery))
        unNotifiedRiskMatchesQuery.addListener(unNotifiedRiskMatchesListener)
        awaitClose {
            unNotifiedRiskMatchesQuery.removeListener(unNotifiedRiskMatchesListener)
        }
    }

    private fun queryNotNotifiedRiskMatches(query: Query<RiskMatchTable>): List<RiskMatch> {
        return query.executeAsList().map { it.toRiskMatch() }
    }
    
    override suspend fun markAsNotifiedRiskMatches(riskMatches: List<RiskMatch>) {
        riskMatchNotificationQueries.transaction {
            riskMatches.forEach { riskMatch ->
                riskMatchNotificationQueries.markAsHandled(
                    regionId = riskMatch.regionId,
                    visitId = riskMatch.visitId,
                    dayStartTimestampSec = riskMatch.dayStartTimestampSec
                )
            }
        }
    }
    
    private fun RiskMatch.toRiskMatchTable(): RiskMatchTable =
        RiskMatchTable(
            regionId = regionId,
            visitId = visitId,
            visit = visit,
            severity = severity.toLong(),
            dayStartTimestampSec = dayStartTimestampSec
        )

    private fun RiskMatchTable.toRiskMatch(): RiskMatch =
        RiskMatch(
            regionId = regionId,
            visitId = visitId,
            visit = visit,
            severity = severity.toInt(),
            dayStartTimestampSec = dayStartTimestampSec,
        )

}
