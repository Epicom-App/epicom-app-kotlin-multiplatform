package org.ebolapp.features.visits.db

import com.squareup.sqldelight.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.ebolapp.db.UserLocationTable
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.utils.DateUtils

interface UserLocationsDbApi {

    suspend fun getUserLocationsBetweenTimestamp(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<UserLocation>

    suspend fun addUserLocations(userLocations: List<UserLocation>)
    suspend fun deleteUserLocationsBeforeTimestamp(timestampSec: Long)
    fun observeUserLocationsForToday(
        timestampSecNow: Long = DateUtils.nowTimestampSec()
    ): Flow<List<UserLocation>>

    suspend fun getLastUserLocation(): UserLocation?

}

internal class UserLocationsDbApiImpl(
    databaseWrapper: DatabaseWrapper
) : UserLocationsDbApi {

    private val database = databaseWrapper.database

    private val userLocationQueries = database.userLocationTableQueries

    // User locations
    override suspend fun getUserLocationsBetweenTimestamp(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<UserLocation> {
        return userLocationQueries
            .selectBetweenTimestamps(startTimestampSec, endTimestampSec)
            .executeAsList()
            .map { userLocationTable -> userLocationTable.toUserLocation() }
    }

    override suspend fun addUserLocations(userLocations: List<UserLocation>) {
        val dbLocations = userLocations.map { userLocation ->
            userLocation.toUserLocationTable()
        }
        userLocationQueries.transaction {
            dbLocations.forEach {
                userLocationQueries.insertUserLocation(it)
            }
        }
    }

    override suspend fun deleteUserLocationsBeforeTimestamp(timestampSec: Long) {
        userLocationQueries.deleteAllBeforeTimestamp(timestampSec)
    }

    override fun observeUserLocationsForToday(
        timestampSecNow: Long
    ): Flow<List<UserLocation>> = callbackFlow {
        val timestampSecDayStart = DateUtils.dayStartTimestamp(timestampSecNow)
        val userLocationsQuery = userLocationQueries.selectBetweenTimestamps(
            timestampSecDayStart,
            Long.MAX_VALUE
        )
        val userLocationsTableChangeListener = object : Query.Listener {
            override fun queryResultsChanged() {
                this@callbackFlow.offer(queryUserLocations(userLocationsQuery))
            }
        }
        offer(queryUserLocations(userLocationsQuery))
        userLocationsQuery.addListener(userLocationsTableChangeListener)
        awaitClose {
            userLocationsQuery.removeListener(userLocationsTableChangeListener)
            close()
        }
    }

    private fun queryUserLocations(query: Query<UserLocationTable>): List<UserLocation> {
        return query.executeAsList().map { userLocationTable -> userLocationTable.toUserLocation() }
    }

    override suspend fun getLastUserLocation(): UserLocation? {
        return userLocationQueries
            .selectLastUserLocation()
            .executeAsOneOrNull()
            ?.toUserLocation()
    }

    private fun UserLocation.toUserLocationTable(): UserLocationTable =
        UserLocationTable(
            lat = position.lat,
            lon = position.lon,
            timestampSec = timestampSec
        )

    private fun UserLocationTable.toUserLocation(): UserLocation =
        UserLocation(
            position = Position(
                lat = lat,
                lon = lon
            ),
            timestampSec = timestampSec
        )

}