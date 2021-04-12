package org.ebolapp.features.debug.usecases

import org.ebolapp.db.RiskMatchTable
import org.ebolapp.db.DatabaseWrapper

interface GetRiskMatchDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<RiskMatchTable> 
}

internal class GetRiskMatchDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
) : GetRiskMatchDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(): List<RiskMatchTable> {
        return databaseWrapper
                .database
                .riskMatchTableQueries
                .selectAll()
                .executeAsList()
    }

}