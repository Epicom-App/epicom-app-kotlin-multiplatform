package org.ebolapp.features.regions.test.data

import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.cases.test.data.MapRegionsCasesTestUseCaseFactory
import org.ebolapp.features.cases.test.data.MockRegionCasesDbApi
import org.ebolapp.features.cases.test.data.MockHttpRequestHandler
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.api.JsonDistrictsParser
import org.ebolapp.features.regions.utils.JsonFileReader
import org.ebolapp.features.regions.api.JsonParserImpl
import org.ebolapp.features.regions.api.JsonStatesParser
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.usecases.*

class MapRegionsTestUseCaseFactory {

     fun mapRegionsCasesTestUseCaseFactory(
        endpoints: Endpoints,
        mockNetworkApiResponseHandler: MockHttpRequestHandler,
        mockCasesDbApi: CasesDbApi = MockRegionCasesDbApi()
    ) : MapRegionsCasesTestUseCaseFactory {
        return MapRegionsCasesTestUseCaseFactory(
            endpoints = endpoints,
            requestHandler = mockNetworkApiResponseHandler,
            mockCasesDbApi = mockCasesDbApi
        )
    }

    private fun createStatesParser(fileReader: JsonFileReader): JsonStatesParser =
        JsonParserImpl(fileReader)

    private fun createDistrictsParser(fileReader: JsonFileReader): JsonDistrictsParser =
        JsonParserImpl(fileReader)
    
    fun createCreateMapRegionsUseCase(
        regionsDbApi: RegionsDbApi,
        statesFileReader: JsonFileReader,
        districtFileReader: JsonFileReader
    ): CreateMapRegionsUseCase {
        return CreateMapRegionsUseCaseImpl(
            regionsDbApi = regionsDbApi,
            statesParser = createStatesParser(statesFileReader),
            districtsParser = createDistrictsParser(districtFileReader)
        )
    }

    fun createGetMapRegionsUseCase(
        regionsDbApi: RegionsDbApi,
        getMapRegionCasesByTimestampUseCase: GetMapRegionCasesByTimestampUseCase,
        getMapStatesCasesByTimestampUseCase: GetMapStatesCasesByTimestampUseCase,
        getMapRegionCasesByLegendUseCase: GetMapRegionCasesLegendUseCase
    ): GetMapRegionsUseCase {
        return GetMapRegionsUseCaseImpl(
            databaseApi = regionsDbApi,
            getMapRegionCasesByTimestampUseCase = getMapRegionCasesByTimestampUseCase,
            getMapStatesCasesByTimestampUseCase = getMapStatesCasesByTimestampUseCase,
            getMapRegionCasesLegendUseCase = getMapRegionCasesByLegendUseCase
        )
    }

    fun createGetMapRegionByPositionUseCase(
        regionsDbApi: RegionsDbApi
    ): GetMapRegionIdByPositionUseCase =
        GetMapRegionIdByPositionUseCaseImpl(regionsDbApi)

// Todo: Implement
//    fun createGetMapRegionsWithGeometryUseCase(
//        mockRegionsDbApi: MockRegionsDbApi,
//        getMapRegionUseCase: GetMapRegionsUseCase
//    ): GetMapRegionsWithGeometryUseCase {
//        return GetMapRegionsWithGeometryUseCaseImpl(
//            databaseApi = mockRegionsDbApi,
//            getMapRegionsUseCase = getMapRegionUseCase
//        )
//    }

    fun createGetMapRegionInfoUseCase(
        regionsDbApi: RegionsDbApi,
        getMapRegionCasesByTimestampUseCase: GetMapRegionCasesByTimestampUseCase,
        getMapStatesCasesByTimestampUseCase: GetMapStatesCasesByTimestampUseCase,
        getMapRegionCasesLegendUseCase: GetMapRegionCasesLegendUseCase
    ): GetMapRegionInfoUseCase {
        return GetMapRegionInfoUseCaseImpl(
            regionsDbApi = regionsDbApi,
            getMapRegionCasesUseCase = getMapRegionCasesByTimestampUseCase,
            getMapStatesCasesUseCase = getMapStatesCasesByTimestampUseCase,
            getMapRegionCasesLegendUseCase = getMapRegionCasesLegendUseCase
        )
    }

}