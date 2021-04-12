package org.ebolapp.features.cases.usecases

import io.ktor.client.features.*
import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionCaseCacheKey
import org.ebolapp.features.cases.network.CasesNetworkApi
import org.ebolapp.utils.Constants
import org.ebolapp.utils.DateUtils
import kotlin.native.concurrent.ThreadLocal

interface GetMapRegionCasesByTimestampUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): List<MapRegionCase>
}

class GetMapRegionCasesByTimestampUseCaseImpl(
    private val casesNetworkApi: CasesNetworkApi,
    private val casesDbApi: CasesDbApi
) : GetMapRegionCasesByTimestampUseCase {

    @ThreadLocal
    companion object {
        private var lastRequestTimestampSec: Long = 0
    }

    @Throws(Throwable::class)
    override suspend operator fun invoke(timestampSec: Long): List<MapRegionCase> {

        val dayTimestampSec = DateUtils.dayStartTimestamp(timestampSec)
        val storedCases = casesDbApi.loadMapRegionCasesByTimestamp(timestampSec = dayTimestampSec)

        val currentTimestampSec = DateUtils.nowTimestampSec()
        val timePastFromPrevRequest = currentTimestampSec - lastRequestTimestampSec
        if (storedCases.count() == 0 || timePastFromPrevRequest >= Constants.Cache.MIN_CASES_CACHE_SEC) {

            lastRequestTimestampSec = currentTimestampSec

            val storedCasesCacheKey = casesDbApi.loadMapRegionCasesCacheKey(dayTimestampSec)
            val remoteCasesCacheKey = try {
                casesNetworkApi.getMapRegionCasesCacheKey(dayTimestampSec)
            } catch (x: Exception) {
                return getPrevDayCasesOrCached(x, storedCases, timestampSec)
            }

            if (storedCasesCacheKey != remoteCasesCacheKey)
                return try {
                    casesNetworkApi
                        .getMapRegionCasesByTimestampSec(dayTimestampSec)
                        .let { (cases, casesCacheKey) ->
                            saveAndGetMapRegionCases(cases, casesCacheKey, dayTimestampSec)
                        }
                } catch (x: ClientRequestException) {
                    getPrevDayCasesOrCached(x, storedCases, timestampSec)
                }

        }

        return storedCases
    }

    private suspend fun getPrevDayCasesOrCached(
        exception: Exception,
        storedCases: List<MapRegionCase>,
        timestampSec: Long
    ): List<MapRegionCase> {
        // Try to get data for previous day if data for current day is not uploaded yet to backend
        return if (
            DateUtils.isToday(timestampSec) &&
            exception is ClientRequestException &&
            exception.response.status.value == 404
        ) {
            invoke(DateUtils.timestampAtStartOfDayByAddingDays(timestampSec, days = -1))
        } else {
            // In case we don't have anything cached propagate exception otherwise return cache
            if (storedCases.isEmpty()) throw exception else storedCases
        }
    }

    private suspend fun saveAndGetMapRegionCases(
        cases: List<MapRegionCase>,
        casesCacheKey: MapRegionCaseCacheKey,
        timestampSec: Long
    ): List<MapRegionCase> {
        val casesDeleteBeforeTimestamp = DateUtils.timestampAtStartOfDayByAddingDays(days = -Constants.Cache.CASES_CACHE_DAYS_COUNT)
        casesDbApi.deleteCasesCacheBeforeTimestamp(casesDeleteBeforeTimestamp)
        casesDbApi.saveMapRegionCases(cases, timestampSec)
        casesDbApi.saveMapRegionCasesCacheKey(casesCacheKey)
        return cases
    }
}