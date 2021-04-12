package org.ebolapp.features.cases.usecases

import io.ktor.client.features.*
import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.network.CasesNetworkApi
import org.ebolapp.utils.DateUtils

@Deprecated(message = "Method is deprecated. Use GetMapRegionCasesByTimestampUseCase instead.")
interface SaveMapRegionsCasesByTimestampSecUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(timestampSec: Long): Boolean
}

class SaveMapRegionsCasesByTimestampSecUseCaseImpl(
    private val getMapRegionCasesByTimestampUseCase: GetMapRegionCasesByTimestampUseCase
) : SaveMapRegionsCasesByTimestampSecUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(timestampSec: Long): Boolean {
        return getMapRegionCasesByTimestampUseCase(timestampSec = timestampSec).count() > 0
    }
}