package org.ebolapp.features.debug.usecases

import org.ebolapp.db.RiskMatchNotificationTable
import org.ebolapp.db.DatabaseWrapper

interface GetRiskMatchNotificationDbgInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): List<RiskMatchNotificationTable>
}

internal class GetRiskMatchNotificationDbgInfoUseCaseImpl(
    private val databaseWrapper: DatabaseWrapper
): GetRiskMatchNotificationDbgInfoUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(): List<RiskMatchNotificationTable> {
        return databaseWrapper
                .database
                .riskMatchNotificationTableQueries
                .selectAll()
                .executeAsList()
    }
}