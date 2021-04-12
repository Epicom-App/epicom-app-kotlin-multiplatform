package org.ebolapp.features.regions.usecases

import org.ebolapp.features.cases.usecases.GetMapRegionCasesByTimestampUseCase
import org.ebolapp.features.cases.usecases.GetMapRegionCasesLegendUseCase
import org.ebolapp.features.cases.usecases.GetMapStatesCasesByTimestampUseCase
import org.ebolapp.features.regions.db.RegionsDbApi
import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.utils.Constants

interface GetMapRegionInfoUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(regionId: String, timestampSec: Long): MapRegionInfo?
}

internal class GetMapRegionInfoUseCaseImpl(
    private val regionsDbApi: RegionsDbApi,
    private val getMapRegionCasesUseCase: GetMapRegionCasesByTimestampUseCase,
    private val getMapStatesCasesUseCase: GetMapStatesCasesByTimestampUseCase,
    private val getMapRegionCasesLegendUseCase: GetMapRegionCasesLegendUseCase
): GetMapRegionInfoUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(regionId: String, timestampSec: Long): MapRegionInfo? {
        val legend = getMapRegionCasesLegendUseCase()
        val maxSeverityItem = legend.items.maxByOrNull { it.severity }!!
        val region = regionsDbApi.getMapRegion(regionId)
        return if (region.parentId != null) {
            val case = getMapRegionCasesUseCase(timestampSec).first { it.areaId == regionId }
            val legendItem = legend.items.firstOrNull { it.severity == case.severity } ?: maxSeverityItem
            MapRegionInfo(
                id = region.id,
                name = region.name,
                severity = legendItem.severity,
                maxSeverity = maxSeverityItem.severity,
                isRisky = legendItem.isRisky,
                severityInfo = legend.name,
                color = legendItem.color,
                casesNumber = case.numberOfCases,
                casesNumberInfo = legendItem.info,
                disease = Constants.Strings.covid19,
                informationUrl = case.informationUrl
            )
        } else getMapStatesCasesUseCase(timestampSec).firstOrNull { it.stateId.toString() == regionId }
            ?.let { case ->
                val legendItem = legend.items.firstOrNull { it.severity == case.severity } ?: maxSeverityItem
                MapRegionInfo(
                    id = regionId,
                    name = region.name,
                    severity = legendItem.severity,
                    maxSeverity = maxSeverityItem.severity,
                    isRisky = legendItem.isRisky,
                    severityInfo = legend.name,
                    color = legendItem.color,
                    casesNumber = case.numberOfCases,
                    casesNumberInfo = legendItem.info,
                    disease = Constants.Strings.covid19,
                    informationUrl = case.informationUrl
                )
            }
    }

}
