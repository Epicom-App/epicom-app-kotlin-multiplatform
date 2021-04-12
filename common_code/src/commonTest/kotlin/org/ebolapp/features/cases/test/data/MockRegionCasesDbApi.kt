package org.ebolapp.features.cases.test.data

import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionCaseCacheKey
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapStateCase

open class MockRegionCasesDbApi : CasesDbApi {

    var getMapRegionCasesBetweenTimestampsResult: List<MapRegionCase> = emptyList()
    var getRiskAreaCasesBetweenTimestampsCalled = false
    var deleteCasesCacheBeforeTimestampCalled = false

    var addRiskAreaCasesCalled = false

    override suspend fun loadMapRegionCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapRegionCase> {
        getRiskAreaCasesBetweenTimestampsCalled = true
        return getMapRegionCasesBetweenTimestampsResult

    }

    override suspend fun loadMapRegionCasesByTimestamp(timestampSec: Long): List<MapRegionCase> {
        getRiskAreaCasesBetweenTimestampsCalled = true
        return getMapRegionCasesBetweenTimestampsResult
    }

    override suspend fun loadMapRegionCasesCacheKey(dayStartTimestampSec: Long): MapRegionCaseCacheKey? {
        return MapRegionCaseCacheKey(timestampSec = dayStartTimestampSec, eTag = "testETag")
    }

    override suspend fun saveMapRegionCasesCacheKey(cacheKey: MapRegionCaseCacheKey) {

    }

    override suspend fun saveMapRegionCases(
        mapRegionCases: List<MapRegionCase>,
        timestampSec: Long
    ) {
        addRiskAreaCasesCalled = true
    }

    override suspend fun loadMapStatesCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapStateCase> {
        TODO("Not yet implemented")
    }

    override suspend fun loadMapStatesCasesByTimestamp(timestampSec: Long): List<MapStateCase> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMapStatesCases(
        mapRegionCases: List<MapStateCase>,
        timestampSec: Long
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteCasesCacheBeforeTimestamp(timestampSec: Long) {
        deleteCasesCacheBeforeTimestampCalled = true
    }

    override suspend fun loadMapRegionLegend(): MapRegionLegend? {
        TODO("Not yet implemented")
    }

    override suspend fun saveMapRegionLegend(legend: MapRegionLegend) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMapRegionLegend() {
        TODO("Not yet implemented")
    }

}
