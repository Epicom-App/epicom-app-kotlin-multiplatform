package org.ebolapp.features.cases.network

import io.ktor.client.HttpClient
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.ebolapp.features.cases.entities.MapRegionCase
import org.ebolapp.features.cases.entities.MapRegionCaseCacheKey
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.entities.MapStateCase
import org.ebolapp.shared.network.NetworkApiParameter
import org.ebolapp.shared.network.NetworkApiParameterNames
import org.ebolapp.utils.DateUtils
import org.ebolapp.utils.Language

interface CasesNetworkApi {
    suspend fun getMapStatesCasesByTimestampSec(timestampSec: Long): List<MapStateCase>
    suspend fun getMapRegionCasesByTimestampSec(timestampSec: Long): Pair<List<MapRegionCase>,MapRegionCaseCacheKey>
    suspend fun getMapRegionCasesCacheKey(timestampSec: Long): MapRegionCaseCacheKey?
    suspend fun getMapRegionCasesLegend(): MapRegionLegend
}

internal open class CasesNetworkApiImpl(
    private val endpoints: Endpoints,
    private val httpClient: HttpClient
) : CasesNetworkApi {

    override suspend fun getMapStatesCasesByTimestampSec(timestampSec: Long): List<MapStateCase> =
        httpClient.get<HttpResponse>(endpoints.mapStatesCasesURL(timestampSec)).receive<List<MapStateCase>>()

    override suspend fun getMapRegionCasesByTimestampSec(timestampSec: Long): Pair<List<MapRegionCase>,MapRegionCaseCacheKey> {
        val response = httpClient.get<HttpResponse>(endpoints.mapRegionCasesURL(timestampSec))

        val mapRegionCase = response.receive<List<MapRegionCase>>()
        val mapRegionCaseCacheKey = getMapRegionCasesCacheKeyFromHeader(response, timestampSec)

        return mapRegionCase to mapRegionCaseCacheKey
    }

    override suspend fun getMapRegionCasesCacheKey(timestampSec: Long): MapRegionCaseCacheKey? {
        val response = httpClient.head<HttpResponse>(endpoints.mapRegionCasesURL(timestampSec))
        return getMapRegionCasesCacheKeyFromHeader(response, timestampSec)
    }

    override suspend fun getMapRegionCasesLegend(): MapRegionLegend {
        return httpClient.get(endpoints.mapRegionCasesLegendURL)
    }

    private fun getMapRegionCasesCacheKeyFromHeader(
        response: HttpResponse,
        timestampSec: Long
    ) : MapRegionCaseCacheKey {
        return MapRegionCaseCacheKey(
            eTag = response.etag() ?: timestampSec.toString(),
            timestampSec = timestampSec
        )
    }
}