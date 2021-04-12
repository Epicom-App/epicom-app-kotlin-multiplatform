package org.ebolapp.features.visits.usecases.locations

import org.ebolapp.features.visits.db.UserLocationsDbApi
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.utils.DateUtils

interface GetUserLocationsForDayByTimestampUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(byTimeStamp: Long) : List<UserLocation>
}

class GetUserLocationsForDayByTimestampUseCaseImpl(
    private val userLocationsDbApi: UserLocationsDbApi
): GetUserLocationsForDayByTimestampUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(byTimeStamp: Long): List<UserLocation> {
        return userLocationsDbApi.getUserLocationsBetweenTimestamp(
            startTimestampSec = DateUtils.dayStartTimestamp(byTimeStamp),
            endTimestampSec = DateUtils.dayEndTimestamp(byTimeStamp)
        )
    }
}