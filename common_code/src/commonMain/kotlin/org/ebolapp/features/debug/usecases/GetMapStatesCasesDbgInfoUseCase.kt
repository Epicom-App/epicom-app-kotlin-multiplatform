package org.ebolapp.features.debug.usecases

import org.ebolapp.db.MapStatesCaseTable
import org.ebolapp.db.DatabaseWrapper

interface GetMapStatesCasesDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<MapStatesCaseTable>
}

class GetMapStatesCasesDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
): GetMapStatesCasesDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(): List<MapStatesCaseTable> {
        return databaseWrapper
            .database
            .mapStatesCaseTableQueries
            .selectAll()
            .executeAsList()
    }

}