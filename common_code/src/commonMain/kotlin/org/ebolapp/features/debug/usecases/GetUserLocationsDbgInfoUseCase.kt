package org.ebolapp.features.debug.usecases

import org.ebolapp.db.UserLocationTable
import org.ebolapp.db.DatabaseWrapper

interface GetUserLocationsDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<UserLocationTable>
}

internal class GetUserLocationsDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
):GetUserLocationsDbgInfoUseCase {
    @Throws(Throwable::class)
    override suspend fun invoke(): List<UserLocationTable> {
        return databaseWrapper
                .database
                .userLocationTableQueries
                .selectAll()
                .executeAsList()
    }
}