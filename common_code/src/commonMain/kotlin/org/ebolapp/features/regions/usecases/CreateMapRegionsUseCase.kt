package org.ebolapp.features.regions.usecases

import org.ebolapp.features.regions.api.DistrictRegion
import org.ebolapp.features.regions.api.JsonDistrictsParser
import org.ebolapp.features.regions.api.JsonStatesParser
import org.ebolapp.features.regions.api.StateRegion
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.utils.MapUtils

interface CreateMapRegionsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke()
}

internal class CreateMapRegionsUseCaseImpl(
    private val regionsDbApi: RegionsDbApi,
    private val statesParser: JsonStatesParser,
    private val districtsParser: JsonDistrictsParser
): CreateMapRegionsUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke() {
        if (areMapRegionsAlreadyCreated()) return
        deleteMapRegions()
        saveStateRegions(statesParser.parseStates())
        saveDistrictRegions(districtsParser.parseDistricts())
    }

    private suspend fun areMapRegionsAlreadyCreated() : Boolean {
        return regionsDbApi.countParentMapRegions() > 0 &&
               regionsDbApi.countChildMapRegions() > 0
    }

    private suspend fun deleteMapRegions() {
        regionsDbApi.deleteMapRegions()
    }

    private suspend fun saveStateRegions(statesRegions: List<StateRegion>) {
        statesRegions
            .associateBy({ it }, { MapUtils.calculateMapRegionBox(it.geoRing) })
            .also { stateRegionWithMapBox ->
                regionsDbApi.insertParentMapRegion(stateRegionWithMapBox)
            }
    }

    private suspend fun saveDistrictRegions(districtRegions: List<DistrictRegion>) {
        districtRegions
            .associateBy({ it }, {MapUtils.calculateMapRegionBox(it.geoRing)})
            .also { districtRegionWithMapBox ->
                regionsDbApi.insertChildMapRegion(districtRegionWithMapBox)
            }
    }
}
