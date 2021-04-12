package org.ebolapp.features.visits.usecases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.utils.DateUtils

interface ObserveVisitsUseCase {
    operator fun invoke(
        fromTimestampSec: Long = DateUtils.dayStartTimestamp(DateUtils.nowTimestampSec()),
        minimalVisitDuration: Long = Visit.MIN_VISIT_DURATION_SEC
    ): Flow<List<Visit>>
}

internal class ObserveVisitsUseCaseImpl(
    private val visitsDbApi: VisitsDbApi
) : ObserveVisitsUseCase {
    override fun invoke(
        fromTimestampSec: Long,
        minimalVisitDuration: Long
    ): Flow<List<Visit>> = visitsDbApi
        .observeVisits(
            timestampSecDayStart = fromTimestampSec,
            minVisitDurationSec = minimalVisitDuration
        )
        .map { visits ->
            visits.filter { it.durationSec() >= minimalVisitDuration }
        }
}
