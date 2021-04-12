package org.ebolapp.utils

object Constants {

    object Strings {

        const val covid19 = "Covid-19"
    }

    object Map {

        const val minStatesFetchDistanceKm = 500.0
    }

    object VisitsRange {
        const val VISIT_RANGE_METERS = 500
    }

    object Cache {
        const val CASES_CACHE_DAYS_COUNT: Int = 20
        const val LOCATIONS_CACHE_DAYS_COUNT: Int = 20
        const val MIN_CASES_CACHE_SEC: Int = 3600
    }
}