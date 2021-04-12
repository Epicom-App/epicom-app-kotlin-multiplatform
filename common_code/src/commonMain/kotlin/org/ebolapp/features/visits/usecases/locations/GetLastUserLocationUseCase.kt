package org.ebolapp.features.visits.usecases.locations

import org.ebolapp.features.visits.db.UserLocationsDbApi
import org.ebolapp.features.visits.entities.UserLocation

interface GetLastUserLocationUseCase {

    @Throws(Throwable::class)
    suspend operator fun invoke() : UserLocation?
}

class GetLastUserLocationUseCaseImpl(
    private val userLocationsDbApi: UserLocationsDbApi
) : GetLastUserLocationUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(): UserLocation? {
        return userLocationsDbApi.getLastUserLocation()
    }
}