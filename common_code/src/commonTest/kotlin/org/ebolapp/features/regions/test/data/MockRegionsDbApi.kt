package org.ebolapp.features.regions.test.data

import org.ebolapp.db.MapRegionTable
import org.ebolapp.features.regions.api.DistrictRegion
import org.ebolapp.features.regions.api.StateRegion
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.features.regions.entities.Position

open class MockRegionsDbApi : RegionsDbApi {

    override suspend fun getMapRegion(regionId: String): MapRegionTable {
        TODO("Not yet implemented")
    }

    override suspend fun getMapRegions(
        regionBox: MapRegionBox,
        statesOnly: Boolean
    ): List<MapRegionTable> {
        TODO("Not yet implemented")
    }

    override suspend fun getMapRegionChildren(parentId: String): List<MapRegionTable> {
        TODO("Not yet implemented")
    }

    override suspend fun getMapRegionGeometry(regionId: String): List<List<Position>> {
        TODO("Not yet implemented")
    }

    override suspend fun getMapRegionByPosition(position: Position): List<MapRegionTable> {
        TODO("Not yet implemented")
    }

    override suspend fun countParentMapRegions(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun countChildMapRegions(): Long {
        TODO("Not yet implemented")
    }

    override suspend fun insertParentMapRegion(parentMapRegions: Map<StateRegion, MapRegionBox>) {
        TODO("Not yet implemented")
    }

    override suspend fun insertChildMapRegion(childMapRegions: Map<DistrictRegion, MapRegionBox>) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteMapRegions() {
        TODO("Not yet implemented")
    }

}