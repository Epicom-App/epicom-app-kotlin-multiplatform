package org.ebolapp.features.visits.test.data

import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.UserLocation
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.utils.DateUtils

object UpdateVisitsTestData {
    
    object LastVisitIsNull {
        
        val userLocation = UserLocation(
            position = Position(lat = 43.434343, lon = 11.213123),
            timestampSec = DateUtils.nowTimestampSec()
        )
        
        val expectVisit = Visit(
            id = 0,
            position = userLocation.position,
            startTimestamp = userLocation.timestampSec,
            endTimestamp = userLocation.timestampSec
        )
    }

    object LastVisitInRangeOfNewUserLocation {

        val newUserLocationTimestamp = 1605636000L // 11/17/2020 @ 6:00pm (UTC)
        val newUserLocation = UserLocation(
            position = Position(lat = 43.434343, lon = 11.213123),
            timestampSec = newUserLocationTimestamp
        )

        val lastVisitTimestamp = 1605614400L

        val lastVisit = Visit(
            id = 0,
            position = newUserLocation.position,
            startTimestamp = lastVisitTimestamp,
            endTimestamp = lastVisitTimestamp
        )

        val expectUpdatedVisit = Visit(
            id = 0,
            position = newUserLocation.position,
            startTimestamp = lastVisitTimestamp,
            endTimestamp = newUserLocationTimestamp
        )
    }

    object LastVisitNotInRangeOfNewUserLocation {

        val newUserLocationTimestamp = 1605636000L // 11/17/2020 @ 6:00pm (UTC)
        val newUserLocation = UserLocation(
            position = Position(lat = 43.430754, lon = 11.228955),
            timestampSec = newUserLocationTimestamp
        )

        val lastVisitTimestamp = 1605614400L
        val lastVisit = Visit(
            id = 0,
            position =Position(lat = 43.434343,lon = 11.213123),
            startTimestamp = lastVisitTimestamp,
            endTimestamp = lastVisitTimestamp
        )

        val expectUpdatedVisit = Visit(
            id = 1,
            position = newUserLocation.position,
            startTimestamp = newUserLocationTimestamp,
            endTimestamp = newUserLocationTimestamp
        )
    }

}