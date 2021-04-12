package org.ebolapp.features.visits.test.data

import kotlinx.coroutines.flow.Flow
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.Visit

open class MockVisitsDbApi : VisitsDbApi {

    override suspend fun getLastVisit(): Visit? {
        TODO("Not yet implemented")
    }

    override suspend fun deleteVisitsBeforeTimestamp(timestampSec: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun getVisitBeforeVisitWithId(id: Long, minDuration: Long): Visit? {
        TODO("Not yet implemented")
    }

    override suspend fun getVisitsBetween(
        startTimestampSec: Long,
        endTimestampSec: Long,
        minVisitDurationSec: Long
    ): List<Visit> {
        TODO("Not yet implemented")
    }

    override suspend fun getVisitsAfter(timestampSec: Long, minVisitDurationSec: Long): List<Visit> {
        TODO("Not yet implemented")
    }

    override fun observeVisits(timestampSecDayStart: Long, minVisitDurationSec: Long): Flow<List<Visit>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertVisit(visit: Visit) {
        TODO("Not yet implemented")
    }

    override suspend fun updateVisit(visit: Visit) {
        // TODO Not yet implemented
    }
}