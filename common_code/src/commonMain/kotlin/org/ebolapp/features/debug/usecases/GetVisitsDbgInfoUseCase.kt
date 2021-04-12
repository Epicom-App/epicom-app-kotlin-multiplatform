package org.ebolapp.features.debug.usecases

import org.ebolapp.db.VisitTable
import org.ebolapp.db.DatabaseWrapper

interface GetVisitsDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke() : List<VisitTable> 
}

internal class GetVisitDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
): GetVisitsDbgInfoUseCase {
    @Throws(Throwable::class)
    override suspend fun invoke(): List<VisitTable> {
        return databaseWrapper
                .database
                .visitTableQueries
                .selectAll()
                .executeAsList()
    }
}