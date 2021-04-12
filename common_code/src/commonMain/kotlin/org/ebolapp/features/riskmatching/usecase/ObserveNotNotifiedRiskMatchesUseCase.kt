package org.ebolapp.features.riskmatching.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.entities.RiskMatch

interface ObserveNotNotifiedRiskMatchesUseCase {
    operator fun invoke() : Flow<List<RiskMatch>>
    fun observe(onUpdate: (List<RiskMatch>) -> Unit)
}

internal class ObserveNotNotifiedRiskMatchesUseCaseImpl(
    private val riskMatchDbApi: RiskMatchDbApi
): ObserveNotNotifiedRiskMatchesUseCase {

    override fun invoke(): Flow<List<RiskMatch>> =
        riskMatchDbApi.observeUnNotifiedRiskMatches().map { newRiskMatches ->

            val unNotifiedRiskMatches = newRiskMatches.sortedBy { it.dayStartTimestampSec }

            // Skip last risk match as it is continuously updated
            if (unNotifiedRiskMatches.isEmpty())
                emptyList()
            else
                unNotifiedRiskMatches - unNotifiedRiskMatches.last()
        }

    override fun observe(onUpdate: (List<RiskMatch>) -> Unit) {
        GlobalScope.launch {
            invoke()
                .flowOn(Dispatchers.Main)
                .collectLatest { matches ->
                    onUpdate(matches)
                }
        }
    }
    
}
