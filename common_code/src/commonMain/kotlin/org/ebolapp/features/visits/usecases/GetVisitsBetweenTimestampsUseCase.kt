package org.ebolapp.features.visits.usecases

import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.Visit


interface GetVisitsBetweenTimestampsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(
        startTimestampSec: Long,
        endTimestampSec: Long,
        minimalVisitDuration: Long = Visit.MIN_VISIT_DURATION_SEC
    ): List<Visit>
}

class GetVisitsBetweenTimestampsUseCaseImpl(
    private val visitsDbApi: VisitsDbApi
) : GetVisitsBetweenTimestampsUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(
        startTimestampSec: Long,
        endTimestampSec: Long,
        minimalVisitDuration: Long
    ): List<Visit> {
        return visitsDbApi.getVisitsBetween(
            startTimestampSec,
            endTimestampSec,
            Visit.MIN_VISIT_DURATION_SEC
        ).filter {
            it.durationSec() >= minimalVisitDuration
        }
    }
}
