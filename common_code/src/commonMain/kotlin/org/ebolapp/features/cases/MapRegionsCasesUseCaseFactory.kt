package org.ebolapp.features.cases

import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.db.CasesDbApiImpl
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.cases.network.CasesNetworkApi
import org.ebolapp.features.cases.network.CasesNetworkApiImpl
import org.ebolapp.features.cases.usecases.*
import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.shared.network.AppHttpClientImpl

class MapRegionsCasesUseCaseFactory(
    endpoints: Endpoints,
    databaseWrapper: DatabaseWrapper
) {

    private val httpClient = AppHttpClientImpl().getClient()

    private val casesDatabaseApi : CasesDbApi = CasesDbApiImpl(databaseWrapper)
    private val casesNetworkApi: CasesNetworkApi = CasesNetworkApiImpl(endpoints, httpClient)

    fun createGetRegionCases() : GetMapRegionCasesByTimestampUseCase {
        return GetMapRegionCasesByTimestampUseCaseImpl(
            casesNetworkApi = casesNetworkApi,
            casesDbApi = casesDatabaseApi
        )
    }

    fun createGetStatesCases() : GetMapStatesCasesByTimestampUseCase {
        return GetMapStatesCasesByTimestampUseCaseImpl(
            casesNetworkApi = casesNetworkApi,
            casesDbApi = casesDatabaseApi
        )
    }

    fun createGetLegendUseCase() : GetMapRegionCasesLegendUseCase {
        return GetMapRegionCasesLegendUseCaseImpl(
            casesNetworkApi = casesNetworkApi,
            casesDbApi = casesDatabaseApi
        )
    }

    fun createSaveCases() : SaveMapRegionsCasesByTimestampSecUseCase =
        SaveMapRegionsCasesByTimestampSecUseCaseImpl(
            getMapRegionCasesByTimestampUseCase = createGetRegionCases()
        )
}