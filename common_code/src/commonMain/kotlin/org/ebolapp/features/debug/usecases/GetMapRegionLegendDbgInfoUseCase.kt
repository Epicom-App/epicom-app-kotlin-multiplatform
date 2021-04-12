package org.ebolapp.features.debug.usecases

import org.ebolapp.db.MapRegionCaseLegendTable

import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.utils.DateUtils

interface GetMapRegionLegendDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<MapRegionCaseLegendTable>
}

internal class GetMapRegionLegendDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
): GetMapRegionLegendDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(): List<MapRegionCaseLegendTable> {
        return databaseWrapper
                .database
                .mapRegionCaseLegendTableQueries
                .selectAllForTimestamp(DateUtils.dayStartTimestamp())
                .executeAsList()
    }
}