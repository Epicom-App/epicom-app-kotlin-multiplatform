package org.ebolapp.features.visits.usecases

import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.utils.Constants
import org.ebolapp.utils.MapUtils

interface UpdateVisitsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(userLocation: UserLocation)
}

class UpdateVisitsUseCaseImpl(
    private val visitsDbApi: VisitsDbApi
) : UpdateVisitsUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(userLocation: UserLocation) {
        val lastVisit = visitsDbApi.getLastVisit()

        if (lastVisit == null) {
            createVisit(userLocation)
        } else {
            // Update end visit date
            updateVisit(lastVisit, userLocation.timestampSec)
            // If new visit is not in range of last visit create a new one
            if (!lastVisit.isInRangeOf(userLocation)) {
                createVisit(userLocation)
            }
        }
    }

    private suspend fun createVisit(userLocation: UserLocation) {
        visitsDbApi.insertVisit(
            Visit(
                position = userLocation.position,
                startTimestamp = userLocation.timestampSec,
                endTimestamp = userLocation.timestampSec
            )
        )
    }

    private suspend fun updateVisit(visit: Visit, endTimestampSec: Long) {
        visitsDbApi.updateVisit(visit.copy(endTimestamp = endTimestampSec))
    }

    private fun Visit.isInRangeOf(userLocation: UserLocation): Boolean {
        val distance = MapUtils.distanceBetweenInMeters(this.position, userLocation.position)
        return (distance <= Constants.VisitsRange.VISIT_RANGE_METERS)
    }
}