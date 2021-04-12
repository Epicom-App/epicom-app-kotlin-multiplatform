package org.ebolapp.features.cases.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ebolapp.db.MapRegionCaseCacheTable
import org.ebolapp.db.MapRegionCaseTable
import org.ebolapp.db.MapStatesCaseTable
import org.ebolapp.features.cases.entities.*
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.utils.DateUtils

interface CasesDbApi {
    // Map regions cases
    suspend fun loadMapRegionCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapRegionCase>
    suspend fun loadMapRegionCasesByTimestamp(
        timestampSec: Long
    ): List<MapRegionCase>

    suspend fun loadMapStatesCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapStateCase>
    suspend fun loadMapStatesCasesByTimestamp(
        timestampSec: Long
    ): List<MapStateCase>

    suspend fun loadMapRegionCasesCacheKey(dayStartTimestampSec: Long): MapRegionCaseCacheKey?
    suspend fun saveMapRegionCasesCacheKey(cacheKey: MapRegionCaseCacheKey)

    suspend fun saveMapRegionCases(mapRegionCases: List<MapRegionCase>, timestampSec: Long)
    suspend fun saveMapStatesCases(mapRegionCases: List<MapStateCase>, timestampSec: Long)
    suspend fun deleteCasesCacheBeforeTimestamp(timestampSec: Long)

    // Map regions legend
    suspend fun loadMapRegionLegend(): MapRegionLegend?
    suspend fun saveMapRegionLegend(legend: MapRegionLegend)
    suspend fun deleteMapRegionLegend()

}

internal class CasesDbApiImpl(
    databaseWrapper: DatabaseWrapper
) : CasesDbApi {

    private val database = databaseWrapper.database

    private val mapRegionCaseQueries = database.mapRegionCaseTableQueries
    private val mapStatesCaseQueries = database.mapStatesCaseTableQueries
    private val mapRegionCaseCacheQueries = database.mapRegionCaseCacheTableQueries
    private val mapRegionsLegendTableQueries = database.mapRegionCaseLegendTableQueries

    override suspend fun loadMapStatesCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapStateCase> =
        mapStatesCaseQueries
            .selectBetweenTimestamps(startTimestampSec, endTimestampSec)
            .executeAsList()
            .map { table ->
                MapStateCase(
                    informationUrl = table.informationUrl,
                    stateId = table.stateId.toInt(),
                    severity = table.severity,
                    numberOfCases = table.numberOfCases
                )
            }

    override suspend fun loadMapStatesCasesByTimestamp(timestampSec: Long): List<MapStateCase> {
        return loadMapStatesCasesBetweenTimestamps(
            startTimestampSec = DateUtils.dayStartTimestamp(timestampSec),
            endTimestampSec = DateUtils.dayEndTimestamp(timestampSec)
        )
    }

    override suspend fun saveMapStatesCases(
        mapRegionCases: List<MapStateCase>,
        timestampSec: Long
    ) {
        withContext(Dispatchers.Default) {
            mapStatesCaseQueries.transaction {
                mapRegionCases.forEach { mapStateCase ->
                    mapStatesCaseQueries.insertMapStateCase(
                        MapStatesCaseTable(
                            stateId = mapStateCase.stateId.toLong(),
                            severity = mapStateCase.severity,
                            numberOfCases = mapStateCase.numberOfCases,
                            timestsampSec = timestampSec,
                            informationUrl = mapStateCase.informationUrl
                        )
                    )
                }
            }
        }
    }

    override suspend fun loadMapRegionCasesBetweenTimestamps(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<MapRegionCase> = withContext(Dispatchers.Default) {
        mapRegionCaseQueries
            .selectBetweenTimestamps(startTimestampSec, endTimestampSec)
            .executeAsList()
            .map { table ->
                MapRegionCase(
                    areaId = table.areaId,
                    stateId = table.stateId?.toInt(),
                    severity = table.severity,
                    numberOfCases = table.numberOfCases,
                    informationUrl = table.informationUrl
                )
            }
    }

    override suspend fun loadMapRegionCasesByTimestamp(timestampSec: Long): List<MapRegionCase> {
        return loadMapRegionCasesBetweenTimestamps(
            startTimestampSec = DateUtils.dayStartTimestamp(timestampSec),
            endTimestampSec = DateUtils.dayEndTimestamp(timestampSec)
        )
    }

    override suspend fun loadMapRegionCasesCacheKey(dayStartTimestampSec: Long): MapRegionCaseCacheKey? {

            val cacheKey =
                mapRegionCaseCacheQueries
                    .selectTag(dayStartTimestampSec)
                    .executeAsOneOrNull()

            cacheKey?.timestsampSec ?: return null

            return MapRegionCaseCacheKey(
                timestampSec = cacheKey.timestsampSec,
                eTag = cacheKey.eEtag
            )
    }

    override suspend fun saveMapRegionCasesCacheKey(cacheKey: MapRegionCaseCacheKey) {
        mapRegionCaseCacheQueries.transaction {
            mapRegionCaseCacheQueries.upsertTag(
                MapRegionCaseCacheTable(
                    timestsampSec = cacheKey.timestampSec,
                    eEtag = cacheKey.eTag
                )
            )
        }
    }

    override suspend fun saveMapRegionCases(
        mapRegionCases: List<MapRegionCase>,
        timestampSec: Long
    ) = withContext(Dispatchers.Default) {
        mapRegionCaseQueries.transaction {
            mapRegionCases.forEach { riskAreaCase ->
                mapRegionCaseQueries.insertRiskArea(
                    areaId = riskAreaCase.areaId,
                    severity = riskAreaCase.severity,
                    numberOfCases = riskAreaCase.numberOfCases,
                    timestsampSec = timestampSec,
                    stateId = riskAreaCase.stateId?.toLong(),
                    informationUrl = riskAreaCase.informationUrl
                )
            }
        }
    }

    override suspend fun deleteCasesCacheBeforeTimestamp(
        timestampSec: Long
    ) = withContext(Dispatchers.Default) {
        mapRegionCaseQueries.deleteAllBeforeTimestamp(timestampSec)
        mapStatesCaseQueries.deleteAllBeforeTimestamp(timestampSec)
    }

    override suspend fun loadMapRegionLegend(): MapRegionLegend? =
        withContext(Dispatchers.Default) {
            var name = ""
            val mapRegionLegendItemsList = mapRegionsLegendTableQueries.selectAllForTimestamp(DateUtils.dayStartTimestamp()).executeAsList()
            if (mapRegionLegendItemsList.isEmpty()) null
            else
                mapRegionLegendItemsList
                    .map { mapRegionCaseLegendItem ->
                        if (name.isBlank()) name = mapRegionCaseLegendItem.name
                        MapRegionLegendItem(
                            severity = mapRegionCaseLegendItem.severity,
                            info = mapRegionCaseLegendItem.info ?: "",
                            color = mapRegionCaseLegendItem.color ?: "",
                            isRisky = (mapRegionCaseLegendItem.isRisky == 1L)
                        )
                    }.let { items ->
                        MapRegionLegend(
                            name = name,
                            items = items
                        )
                    }
        }

    override suspend fun saveMapRegionLegend(legend: MapRegionLegend) =
        withContext(Dispatchers.Default) {
            val timestampStartOfTheDay = DateUtils.dayStartTimestamp()
            mapRegionsLegendTableQueries.transaction {
                val name = legend.name
                legend.items.forEachIndexed { index, item ->
                    mapRegionsLegendTableQueries.insert(
                        name = name,
                        itemId = index,
                        severity = item.severity,
                        info = item.info,
                        color = item.color,
                        isRisky = item.isRisky.let { if (it) 1 else 0 },
                        timestampSec = timestampStartOfTheDay
                    )
                }
            }
        }

    override suspend fun deleteMapRegionLegend() =
        withContext(Dispatchers.Default) {
            mapRegionsLegendTableQueries.transaction {
                mapRegionsLegendTableQueries.deleteAll()
            }
        }

}
