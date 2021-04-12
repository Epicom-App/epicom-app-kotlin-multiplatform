package org.ebolapp.features.regions.db

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.ebolapp.db.MapRegionGeoRingTable
import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.regions.api.DistrictRegion
import org.ebolapp.features.regions.api.StateRegion
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.Position
import org.ebolapp.db.DatabaseWrapper

interface RegionsDbApi {
    // Map regions
    suspend fun getMapRegion(regionId: String): MapRegionTable
    suspend fun getMapRegions(regionBox: MapRegionBox, statesOnly: Boolean): List<MapRegionTable>
    suspend fun getMapRegionChildren(parentId: String): List<MapRegionTable>
    suspend fun getMapRegionGeometry(regionId: String): List<List<Position>>
    suspend fun getMapRegionByPosition(position: Position): List<MapRegionTable>

    suspend fun countParentMapRegions(): Long
    suspend fun countChildMapRegions(): Long
    suspend fun insertParentMapRegion(parentMapRegions: Map<StateRegion, MapRegionBox>)
    suspend fun insertChildMapRegion(childMapRegions: Map<DistrictRegion, MapRegionBox>)

    suspend fun deleteMapRegions()
}

internal class  RegionsDbApiImpl(
    databaseWrapper: DatabaseWrapper
): RegionsDbApi {

    private val database = databaseWrapper.database

    private val mapRegionQueries = database.mapRegionTableQueries
    private val mapRegionGeoRingQueries = database.mapRegionGeoRingTableQueries

    override suspend fun getMapRegion(regionId: String): MapRegionTable =
        withContext(Dispatchers.Default) {
            mapRegionQueries.selectMapRegionWithId(regionId).executeAsOne()
        }

    override suspend fun getMapRegions(
        regionBox: MapRegionBox,
        statesOnly: Boolean
    ): List<MapRegionTable> =
        withContext(Dispatchers.Default) {
            mapRegionQueries.selectMapRegionsWithBox(
                topLeftLon = regionBox.topLeft.lon,
                topLeftLat = regionBox.topLeft.lat,
                bottomRightLon = regionBox.bottomRight.lon,
                bottomRightLat = regionBox.bottomRight.lat,
                statesOnly = if (statesOnly) 1 else 0
            ).executeAsList()
        }

    override suspend fun getMapRegionChildren(parentId: String): List<MapRegionTable> =
        withContext(Dispatchers.Default) {
            mapRegionQueries.selectMapRegionsWithParentId(parentId).executeAsList()
        }

    override suspend fun getMapRegionGeometry(regionId: String): List<List<Position>> =
        withContext(Dispatchers.Default) {
            mapRegionGeoRingQueries.selectMapRegionGeoRing(regionId).executeAsList().groupBy(
                { it.ringIndex }, { Position(it.lat, it.lon) }
            ).map { it.component2() }
        }

    override suspend fun getMapRegionByPosition(position: Position): List<MapRegionTable> =
        withContext(Dispatchers.Default) {
            mapRegionQueries.selectMapRegionByPosition(
                lat = position.lat,
                lon = position.lon
            ).executeAsList()
        }

    override suspend fun countParentMapRegions(): Long =
        withContext(Dispatchers.Default) {
            mapRegionQueries.countParents().executeAsOne()
        }

    override suspend fun countChildMapRegions(): Long =
        withContext(Dispatchers.Default) {
            mapRegionQueries.countChildren().executeAsOne()
        }

    override suspend fun insertParentMapRegion(
        parentMapRegions: Map<StateRegion, MapRegionBox>
    ) = withContext(Dispatchers.Default) {
        database.transaction {
            parentMapRegions.forEach { (state, stateMapBox) ->
                // MapRegionTable
                mapRegionQueries.insertParent(
                    id = state.id,
                    name = state.name,
                    topLeftLat = stateMapBox.topLeft.lat,
                    topLeftLon = stateMapBox.topLeft.lon,
                    bottomRightLat = stateMapBox.bottomRight.lat,
                    bottomRightLon = stateMapBox.bottomRight.lon
                )
                // MapRegionGeoRing
                state.geoRing.forEachIndexed { ringIndex, stateGeoRing ->
                    stateGeoRing.forEach { (lat, lon) ->
                        mapRegionGeoRingQueries.insertMapRegionGeoRing(
                            MapRegionGeoRingTable(
                                regionId = state.id,
                                ringIndex = ringIndex.toLong(),
                                lat = lat,
                                lon = lon
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun insertChildMapRegion(
        childMapRegions: Map<DistrictRegion, MapRegionBox>
    ) = withContext(Dispatchers.Default) {
        database.transaction {
            childMapRegions.forEach { (district, districtMapBox) ->
                // MapRegionTable
                mapRegionQueries.insertChild(
                    id = district.id,
                    name = district.name,
                    parentId = district.stateId,
                    topLeftLat = districtMapBox.topLeft.lat,
                    topLeftLon = districtMapBox.topLeft.lon,
                    bottomRightLat = districtMapBox.bottomRight.lat,
                    bottomRightLon = districtMapBox.bottomRight.lon
                )
                // MapRegionGeoRing
                district.geoRing.forEachIndexed { ringIndex, districtGeoRing ->
                    districtGeoRing.forEach { (lat, lon) ->
                        mapRegionGeoRingQueries.insertMapRegionGeoRing(
                            MapRegionGeoRingTable(
                                regionId = district.id,
                                ringIndex = ringIndex.toLong(),
                                lat = lat,
                                lon = lon
                            )
                        )
                    }
                }
            }
        }
    }

    override suspend fun deleteMapRegions() =
        withContext(Dispatchers.Default) {
            database.transaction {
                mapRegionQueries.deleteAll()
                mapRegionGeoRingQueries.deleteAll()
            }
        }
}