package org.ebolapp.features.riskmatching.entities

import org.ebolapp.features.regions.entities.MapRegionInfo
import org.ebolapp.features.visits.entities.Visit
import org.ebolapp.utils.DateUtils

data class RiskMatch(
    val regionId: String,
    val visitId: Long,
    val visit: Visit,
    val severity: Int,
    val dayStartTimestampSec: Long
) {

    constructor(
        regionInfo: MapRegionInfo,
        visit: Visit,
        dayStartTimestampSec: Long
    ) : this(
        regionId = regionInfo.id,
        visitId = visit.id,
        visit = visit,
        severity = regionInfo.severity,
        dayStartTimestampSec = dayStartTimestampSec
    )

    fun durationSec() : Long {

        val startTimestamp = if (visit.startTimestamp < dayStartTimestampSec)
            dayStartTimestampSec
         else
             visit.startTimestamp

        val endTimestamp = if (visit.endTimestamp > DateUtils.dayEndTimestamp(dayStartTimestampSec))
                DateUtils.dayEndTimestamp(dayStartTimestampSec)
            else
                visit.endTimestamp

        return endTimestamp - startTimestamp
    }
}