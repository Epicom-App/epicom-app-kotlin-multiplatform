package org.ebolapp.features.regions

import org.ebolapp.features.cases.MapRegionsCasesUseCaseFactory
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.api.JsonDistrictsParser
import org.ebolapp.features.regions.utils.JsonFileReader
import org.ebolapp.features.regions.api.JsonParserImpl
import org.ebolapp.features.regions.api.JsonStatesParser
import org.ebolapp.features.regions.db.RegionsDbApiImpl
import org.ebolapp.features.regions.usecases.*
import org.ebolapp.features.regions.usecases.CreateMapRegionsUseCaseImpl
import org.ebolapp.features.regions.usecases.GetMapRegionsUseCaseImpl
import org.ebolapp.db.DatabaseWrapper

@Suppress("MemberVisibilityCanBePrivate")
class MapRegionsUseCaseFactory(
    databaseWrapper: DatabaseWrapper,
    endpoints: Endpoints
) {

    // Internal dependencies
    private val regionsDbApi: RegionsDbApi = RegionsDbApiImpl(databaseWrapper)

    private val mapRegionsCasesUseCaseFactory = MapRegionsCasesUseCaseFactory(
        endpoints = endpoints,
        databaseWrapper = databaseWrapper
    )

    private fun createGetMapRegionCasesByTimestampUseCase(): GetMapRegionCasesByTimestampUseCase {
        return mapRegionsCasesUseCaseFactory.createGetRegionCases()
    }

    private fun createGetMapStatesCasesByTimestampUseCase(): GetMapStatesCasesByTimestampUseCase {
        return mapRegionsCasesUseCaseFactory.createGetStatesCases()
    }

    private fun createGetMapRegionLegendUseCase() : GetMapRegionCasesLegendUseCase {
        return mapRegionsCasesUseCaseFactory.createGetLegendUseCase()
    }

    private fun createStatesParser(fileReader: JsonFileReader): JsonStatesParser =
        JsonParserImpl(fileReader)

    private fun createDistrictsParser(fileReader: JsonFileReader): JsonDistrictsParser =
        JsonParserImpl(fileReader)

    // Exposed use cases
    fun createCreateMapRegionsUseCase(
        statesFileReader: JsonFileReader,
        districtFileReader: JsonFileReader
    ): CreateMapRegionsUseCase {
        return CreateMapRegionsUseCaseImpl(
            regionsDbApi = regionsDbApi,
            statesParser = createStatesParser(statesFileReader),
            districtsParser = createDistrictsParser(districtFileReader)
        )
    }

    fun createGetMapRegionsUseCase(): GetMapRegionsUseCase {
        return GetMapRegionsUseCaseImpl(
            databaseApi = regionsDbApi,
            getMapRegionCasesByTimestampUseCase = createGetMapRegionCasesByTimestampUseCase(),
            getMapStatesCasesByTimestampUseCase = createGetMapStatesCasesByTimestampUseCase(),
            getMapRegionCasesLegendUseCase = createGetMapRegionLegendUseCase()
        )
    }


    fun createGetMapRegionByPositionUseCase(): GetMapRegionIdByPositionUseCase =
        GetMapRegionIdByPositionUseCaseImpl(regionsDbApi)

    fun createGetMapRegionsWithGeometryUseCase(): GetMapRegionsWithGeometryUseCase {
        return GetMapRegionsWithGeometryUseCaseImpl(
            databaseApi = regionsDbApi,
            getMapRegionsUseCase = createGetMapRegionsUseCase()
        )
    }

    fun createGetMapRegionInfoUseCase(): GetMapRegionInfoUseCase {
        return GetMapRegionInfoUseCaseImpl(
            regionsDbApi = regionsDbApi,
            getMapRegionCasesUseCase = createGetMapRegionCasesByTimestampUseCase(),
            getMapStatesCasesUseCase = createGetMapStatesCasesByTimestampUseCase(),
            getMapRegionCasesLegendUseCase = createGetMapRegionLegendUseCase()
        )
    }

}
