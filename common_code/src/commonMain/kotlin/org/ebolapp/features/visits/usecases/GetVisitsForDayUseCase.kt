package org.ebolapp.features.visits.usecases

import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.utils.DateUtils

interface GetVisitsForDayUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(
        timestampSec: Long,
        minimalVisitDuration: Long = Visit.MIN_VISIT_DURATION_SEC
    ): List<Visit>
}

internal class GetVisitsForDayUseCaseImpl(
    private val visitsDbApi: VisitsDbApi
) : GetVisitsForDayUseCase {
    @Throws(Throwable::class)
    override suspend operator fun invoke(
        timestampSec: Long,
        minimalVisitDuration: Long
    ): List<Visit> {
        val dayStartTimestampSec = DateUtils.dayStartTimestamp(timestampSec)
        val dayEndTimestampSec = DateUtils.dayEndTimestamp(timestampSec)
        return visitsDbApi.getVisitsBetween(
            startTimestampSec = dayStartTimestampSec,
            endTimestampSec = dayEndTimestampSec,
            minVisitDurationSec = minimalVisitDuration
        )
    }
}
