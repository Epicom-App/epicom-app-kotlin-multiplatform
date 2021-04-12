package org.ebolapp.features.visits.db

import com.squareup.sqldelight.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.ebolapp.db.VisitTable
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.utils.DateUtils

interface VisitsDbApi {

    suspend fun getLastVisit(): Visit?
    suspend fun getVisitBeforeVisitWithId(id: Long, minDuration: Long = Visit.MIN_VISIT_DURATION_SEC): Visit?
    suspend fun insertVisit(visit: Visit)
    suspend fun updateVisit(visit: Visit)
    suspend fun deleteVisitsBeforeTimestamp(timestampSec: Long)

    suspend fun getVisitsBetween(
        startTimestampSec: Long,
        endTimestampSec: Long,
        minVisitDurationSec: Long
    ): List<Visit>

    suspend fun getVisitsAfter(
        timestampSec: Long,
        minVisitDurationSec: Long
    ): List<Visit>

    fun observeVisits(
        timestampSecDayStart: Long = DateUtils.dayStartTimestamp(DateUtils.nowTimestampSec()),
        minVisitDurationSec: Long
    ): Flow<List<Visit>>

}

internal class VisitsDbApiImpl(
    databaseWrapper: DatabaseWrapper
) : VisitsDbApi {

    private val database = databaseWrapper.database

    private val visitsTableQueries = database.visitTableQueries

    override suspend fun getLastVisit(): Visit? {
        val lastVisitTable = visitsTableQueries
            .selectLastVisit()
            .executeAsOneOrNull() ?: return null
        return lastVisitTable.toVisit()
    }

    override suspend fun getVisitBeforeVisitWithId(id: Long, minDuration: Long): Visit? =
        visitsTableQueries.selectVisitBeforeVisitWithMinDuration(id, minDuration).executeAsOneOrNull()?.toVisit()

    override suspend fun getVisitsBetween(
        startTimestampSec: Long,
        endTimestampSec: Long,
        minVisitDurationSec: Long
    ): List<Visit> {
        return visitsTableQueries
            .selectVisitsBetweenTimestamps(
                startTimestampSec = startTimestampSec,
                endTimestampSec = endTimestampSec,
                durationSec = minVisitDurationSec
            )
            .executeAsList()
            .map { visitTable -> visitTable.toVisit() }
    }

    override suspend fun getVisitsAfter(timestampSec: Long, minVisitDurationSec: Long): List<Visit> {
        return visitsTableQueries
            .selectVisitsBetweenTimestamps(
                startTimestampSec = timestampSec,
                endTimestampSec = Long.MAX_VALUE,
                durationSec = minVisitDurationSec)
            .executeAsList()
            .map { visitTable -> visitTable.toVisit() }
    }

    override suspend fun deleteVisitsBeforeTimestamp(timestampSec: Long) {
        visitsTableQueries.transaction {
            visitsTableQueries.deleteAllBeforeTimestamp(timestampSec)
        }
    }

    override suspend fun insertVisit(visit: Visit) {
        visitsTableQueries.insertVisit(
            lat = visit.position.lat,
            lon = visit.position.lon,
            startTimestampSec = visit.startTimestamp,
            endTimestampSec = visit.endTimestamp
        )
    }

    override suspend fun updateVisit(visit: Visit) {
        visitsTableQueries.updateVisit(
            id = visit.id,
            lat = visit.position.lat,
            lon = visit.position.lon,
            startTimestampSec = visit.startTimestamp,
            endTimestampSec = visit.endTimestamp
        )
    }

    override fun observeVisits(
        timestampSecDayStart: Long,
        minVisitDurationSec: Long
    ): Flow<List<Visit>> = callbackFlow {

        val visitsQuery = visitsTableQueries.selectVisitsBetweenTimestamps(
            startTimestampSec = timestampSecDayStart,
            endTimestampSec = Long.MAX_VALUE,
            durationSec = Visit.MIN_VISIT_DURATION_SEC
        )
        val visitsTableChangeListener = object : Query.Listener {
            override fun queryResultsChanged() {
                // Provide data on subsequent change
                this@callbackFlow.offer(queryVisits(visitsQuery))
            }
        }
        // Provide data on first collect
        offer(queryVisits(visitsQuery))
        visitsQuery.addListener(visitsTableChangeListener)
        awaitClose {
            visitsQuery.removeListener(visitsTableChangeListener)
            close()
        }
    }

    private fun queryVisits(query: Query<VisitTable>): List<Visit> {
        return query.executeAsList().map { visitTable -> visitTable.toVisit() }
    }

    private fun VisitTable.toVisit(): Visit =
        Visit(
            id = id,
            position = Position(
                lat = lat,
                lon = lon
            ),
            startTimestamp = startTimestampSec,
            endTimestamp = endTimestampSec
        )
}
