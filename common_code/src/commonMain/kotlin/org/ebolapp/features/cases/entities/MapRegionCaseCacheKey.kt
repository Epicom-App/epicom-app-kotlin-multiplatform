package org.ebolapp.features.cases.entities

data class MapRegionCaseCacheKey(
    val timestampSec: Long,
    val eTag: String
)