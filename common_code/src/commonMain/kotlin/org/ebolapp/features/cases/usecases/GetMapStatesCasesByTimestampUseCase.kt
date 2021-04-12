package org.ebolapp.features.cases.usecases

import io.ktor.client.features.*
import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.entities.MapStateCase
import org.ebolapp.features.cases.network.CasesNetworkApi
import org.ebolapp.utils.Constants
import org.ebolapp.utils.DateUtils
import kotlin.native.concurrent.ThreadLocal

interface GetMapStatesCasesByTimestampUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): List<MapStateCase>
}

class GetMapStatesCasesByTimestampUseCaseImpl(
    private val casesNetworkApi: CasesNetworkApi,
    private val casesDbApi: CasesDbApi
) : GetMapStatesCasesByTimestampUseCase {

    @ThreadLocal
    companion object {
        private var lastRequestTimestampSec: Long = 0
    }

    @Throws(Throwable::class)
    override suspend fun invoke(timestampSec: Long): List<MapStateCase> {
        val dayTimestampSec = DateUtils.dayStartTimestamp(timestampSec)
        val storedCases = casesDbApi.loadMapStatesCasesByTimestamp(timestampSec = dayTimestampSec)

        val currentTimestampSec = DateUtils.nowTimestampSec()
        val timePastFromPrevRequest = currentTimestampSec - lastRequestTimestampSec

        if (storedCases.count() == 0 || timePastFromPrevRequest >= Constants.Cache.MIN_CASES_CACHE_SEC) {
            lastRequestTimestampSec = currentTimestampSec
            return try {
                casesNetworkApi
                    .getMapStatesCasesByTimestampSec(dayTimestampSec)
                    .also { cases ->
                        casesDbApi.saveMapStatesCases(cases, timestampSec)
                    }
            } catch (x: ClientRequestException) {
                emptyList()
            }
        }

        return storedCases
    }
}