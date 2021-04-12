package org.ebolapp.features.regions.usecases

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.entities.MapRegionWithGeometry
import org.ebolapp.features.regions.entities.MapRegionBox
import org.ebolapp.utils.isCanceled

interface GetMapRegionsWithGeometryUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(
        mapRegionBox: MapRegionBox,
        timestampSec: Long
    ) : List<MapRegionWithGeometry>

    fun getMapRegions(
        mapRegionBox: MapRegionBox,
        timestampSec: Long,
        onResult: (List<MapRegionWithGeometry>) -> Unit,
        onError: (e: Throwable) -> Unit
    ) : Job
}

internal class GetMapRegionsWithGeometryUseCaseImpl(
    private val databaseApi: RegionsDbApi,
    private val getMapRegionsUseCase: GetMapRegionsUseCase
): GetMapRegionsWithGeometryUseCase {

    override suspend fun invoke(
        mapRegionBox: MapRegionBox,
        timestampSec: Long
    ): List<MapRegionWithGeometry> {
        return getMapRegionsUseCase(mapRegionBox, timestampSec).map {
            MapRegionWithGeometry(
                id = it.id,
                parentId = it.parentId,
                severity = it.severity,
                color = it.color,
                geoRings = databaseApi.getMapRegionGeometry(it.id)
            )
        }
    }

    override fun getMapRegions(
        mapRegionBox: MapRegionBox,
        timestampSec: Long,
        onResult: (List<MapRegionWithGeometry>) -> Unit,
        onError: (e: Throwable) -> Unit
    ): Job {
        return GlobalScope.launch(context = Dispatchers.Main) {
            try {
                val regions = invoke(mapRegionBox, timestampSec)
                onResult(regions)
            } catch (e: Throwable) {
                if (!e.isCanceled())
                    onError(e)
            }
        }
    }
}