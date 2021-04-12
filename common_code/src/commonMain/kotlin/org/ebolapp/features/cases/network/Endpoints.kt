package org.ebolapp.features.cases.network

import org.ebolapp.shared.network.NetworkApiParameter
import org.ebolapp.shared.network.NetworkApiParameterNames
import org.ebolapp.shared.network.NetworkParametersSendStrategy
import org.ebolapp.utils.DateUtils
import org.ebolapp.utils.Language

data class Endpoints(
    private val mapStatesCases: NetworkParametersSendStrategy,
    private val mapRegionCases: NetworkParametersSendStrategy,
    private val mapRegionCasesLegend: NetworkParametersSendStrategy
) {
    fun mapStatesCasesURL(timestampSec: Long): String =
        mapStatesCases.getURL(parameters = listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.TIMESTAMP,
                value = DateUtils.dateForURL(timestampSec)
            )
        ))

    fun mapRegionCasesURL(timestampSec: Long): String =
        mapRegionCases.getURL(parameters = listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.TIMESTAMP,
                value = DateUtils.dateForURL(timestampSec)
            )
        ))

    val mapRegionCasesLegendURL: String
        get() = mapRegionCasesLegend.getURL(parameters = listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.LANGUAGE,
                value = Language().code
            )
        ))
}