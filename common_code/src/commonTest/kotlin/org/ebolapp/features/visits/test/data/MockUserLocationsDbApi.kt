package org.ebolapp.features.visits.test.data

import kotlinx.coroutines.flow.Flow
import org.ebolapp.features.visits.db.UserLocationsDbApi
import org.ebolapp.features.visits.entities.UserLocation

open class MockUserLocationsDbApi : UserLocationsDbApi {

    override suspend fun getUserLocationsBetweenTimestamp(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<UserLocation> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserLocations(userLocations: List<UserLocation>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteUserLocationsBeforeTimestamp(timestampSec: Long) {
        TODO("Not yet implemented")
    }

    override fun observeUserLocationsForToday(timestampSecNow: Long): Flow<List<UserLocation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getLastUserLocation(): UserLocation? {
        TODO("Not yet implemented")
    }
    
}
