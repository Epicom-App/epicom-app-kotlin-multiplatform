package org.ebolapp.features.debug.usecases

import org.ebolapp.db.MapRegionCaseTable
import org.ebolapp.db.DatabaseWrapper

interface GetMapRegionCasesDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<MapRegionCaseTable>
}

class GetMapRegionCasesDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
): GetMapRegionCasesDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(): List<MapRegionCaseTable> {
        return databaseWrapper
            .database
            .mapRegionCaseTableQueries
            .selectAll()
            .executeAsList()
    }

}