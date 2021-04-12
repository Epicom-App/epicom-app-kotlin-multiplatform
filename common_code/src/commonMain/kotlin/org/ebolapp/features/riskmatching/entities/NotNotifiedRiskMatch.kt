package org.ebolapp.features.riskmatching.entities

import org.ebolapp.features.riskmatching.usecase.RiskMatchType

data class NotNotifiedRiskMatch(
    val riskMatch: RiskMatch,
    val type: RiskMatchType
)