package org.ebolapp.features.visits.usecases.locations

import org.ebolapp.features.visits.db.UserLocationsDbApi
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.features.visits.usecases.UpdateVisitsUseCase
import org.ebolapp.utils.Constants
import org.ebolapp.utils.DateUtils

interface SaveUserLocationsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(locations: List<UserLocation>)
}

class SaveUserLocationsUseCaseImpl(
    private val userLocationsDbApi: UserLocationsDbApi,
    private val updateVisitsUseCase: UpdateVisitsUseCase
) : SaveUserLocationsUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(locations: List<UserLocation>) {
        val deleteBeforeTimestamp = DateUtils.timestampAtStartOfDayByAddingDays(days = -Constants.Cache.LOCATIONS_CACHE_DAYS_COUNT)
        userLocationsDbApi.deleteUserLocationsBeforeTimestamp(deleteBeforeTimestamp)
        userLocationsDbApi.addUserLocations(locations)
        locations.forEach { updateVisitsUseCase(it) }
    }
}