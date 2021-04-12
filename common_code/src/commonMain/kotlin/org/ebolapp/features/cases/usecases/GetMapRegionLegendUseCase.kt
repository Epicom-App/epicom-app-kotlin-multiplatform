package org.ebolapp.features.cases.usecases

import org.ebolapp.features.cases.db.CasesDbApi
import org.ebolapp.features.cases.entities.MapRegionLegend
import org.ebolapp.features.cases.network.CasesNetworkApi

interface GetMapRegionCasesLegendUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(): MapRegionLegend
}

class GetMapRegionCasesLegendUseCaseImpl(
    private val casesNetworkApi: CasesNetworkApi,
    private val casesDbApi: CasesDbApi
) : GetMapRegionCasesLegendUseCase {

    @Throws(Throwable::class)
    override suspend operator fun invoke(): MapRegionLegend {
        val cachedLegend = casesDbApi.loadMapRegionLegend()
        if (cachedLegend != null) return cachedLegend
        val networkLegend = casesNetworkApi.getMapRegionCasesLegend()
        casesDbApi.deleteMapRegionLegend()
        casesDbApi.saveMapRegionLegend(networkLegend)
        return casesDbApi.loadMapRegionLegend() ?: throw Exception("Could not get legend")
    }
}