package org.ebolapp.features.cases.test.data

import io.ktor.client.engine.mock.*
import io.ktor.client.request.*
import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.network.CasesNetworkApi
import org.ebolapp.features.cases.network.CasesNetworkApiImpl
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCaseImpl
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCaseImpl
import org.ebolapp.shared.network.MockAppHttpClientImpl
import org.ebolapp.shared.network.NetworkParametersSendStrategy
import org.ebolapp.shared.network.NoParametersStrategy

class MapRegionsCasesTestUseCaseFactory(
    endpoints: Endpoints = Endpoints(
        NoParametersStrategy(""),
        NoParametersStrategy(""),
        NoParametersStrategy("")
    ),
    requestHandler: suspend MockRequestHandleScope.(request: HttpRequestData) -> HttpResponseData = {
        respond("Success")
    },
    private val mockCasesDbApi: CasesDbApi
) {

    private val mockCasesNetworkApi: CasesNetworkApi = CasesNetworkApiImpl(
        endpoints,
        MockAppHttpClientImpl(requestHandler).getClient()
    )

    fun createGetMapRegionCasesByTimestampUseCase() : GetMapRegionCasesByTimestampUseCase {
        return GetMapRegionCasesByTimestampUseCaseImpl(
            casesNetworkApi = mockCasesNetworkApi,
            casesDbApi = mockCasesDbApi
        )
    }

    fun createGetMapRegionCasesLegendUseCase() : GetMapRegionCasesLegendUseCase {
        return GetMapRegionCasesLegendUseCaseImpl(
            casesNetworkApi = mockCasesNetworkApi,
            casesDbApi = mockCasesDbApi
        )
    }
}