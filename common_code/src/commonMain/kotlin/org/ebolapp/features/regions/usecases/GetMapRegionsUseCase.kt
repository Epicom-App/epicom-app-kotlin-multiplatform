package org.ebolapp.features.regions.usecases

import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.entities.MapRegion
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.utils.Constants
import org.ebolapp.utils.MapUtils

interface GetMapRegionsUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(mapRegionBox: MapRegionBox, timestampSec: Long) : List<MapRegion>
}

internal class GetMapRegionsUseCaseImpl(
    private val databaseApi: RegionsDbApi,
    private val getMapRegionCasesByTimestampUseCase: GetMapRegionCasesByTimestampUseCase,
    private val getMapStatesCasesByTimestampUseCase: GetMapStatesCasesByTimestampUseCase,
    private val getMapRegionCasesLegendUseCase: GetMapRegionCasesLegendUseCase
): GetMapRegionsUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(mapRegionBox: MapRegionBox, timestampSec: Long): List<MapRegion> {
        val minDistance = Constants.Map.minStatesFetchDistanceKm
        val statesOnly = MapUtils.distanceBetweenInKm(mapRegionBox.topLeft, mapRegionBox.bottomRight) > minDistance
        val regions =  databaseApi.getMapRegions(mapRegionBox, statesOnly)
        val cases = getMapRegionCasesByTimestampUseCase(timestampSec)
        val stateCases = getMapStatesCasesByTimestampUseCase(timestampSec)
        val legend = getMapRegionCasesLegendUseCase()
        return regions.map { dbRegion ->
            val isDistrict = dbRegion.parentId != null
            val severity: Int = if (isDistrict) {
                cases.first { it.areaId == dbRegion.id }.severity
            } else stateCases.firstOrNull { it.stateId.toString() == dbRegion.id }?.severity ?: let {
                val children = databaseApi.getMapRegionChildren(dbRegion.id).map { it.id }
                cases.filter { children.contains(it.areaId) }.map { it.severity }.average().toInt()
            }
            MapRegion(
                id = dbRegion.id,
                parentId = dbRegion.parentId,
                severity = severity,
                color = legend.items.firstOrNull { it.severity == severity }?.color ?: ""
            )
        }
    }
}