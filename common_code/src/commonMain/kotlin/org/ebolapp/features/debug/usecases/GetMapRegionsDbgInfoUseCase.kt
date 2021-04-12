package org.ebolapp.features.debug.usecases

import org.ebolapp.db.MapRegionTable
import org.ebolapp.db.DatabaseWrapper

interface GetMapRegionsDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<MapRegionTable>
}

internal class  GetMapRegionsDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
) : GetMapRegionsDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(): List<MapRegionTable> {
        return databaseWrapper
                .database
                .mapRegionTableQueries
                .selectAll()
                .executeAsList()
    }

}