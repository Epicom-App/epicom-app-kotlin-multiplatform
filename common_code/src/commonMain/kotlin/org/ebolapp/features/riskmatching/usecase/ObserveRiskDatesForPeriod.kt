package org.ebolapp.features.riskmatching.usecase

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.ebolapp.utils.DateUtils

interface ObserveRiskDatesOffsetsFromCurrentDateForPeriod {
    suspend operator fun invoke(daysPeriod: Int) : Flow<Int>
    fun observe(days: Int, onUpdate: (Int) -> Unit) : Job
}

internal class ObserveRiskDatesOffsetsFromCurrentDateForPeriodImpl(
    private val observeRiskMatchForPeriodUseCase: ObserveRiskMatchForPeriodUseCase
) : ObserveRiskDatesOffsetsFromCurrentDateForPeriod {

    override suspend fun invoke(daysPeriod: Int): Flow<Int> =
        observeRiskMatchForPeriodUseCase.invoke(daysPeriod).mapNotNull { matches ->
            matches.firstOrNull()?.let { DateUtils.daysBetween(it.dayStartTimestampSec, DateUtils.nowTimestampSec()) }
        }

    @ExperimentalCoroutinesApi
    override fun observe(days: Int, onUpdate: (Int) -> Unit) : Job =
        GlobalScope.launch {
            invoke(days)
                .flowOn(Dispatchers.Main)
                .collectLatest {
                    onUpdate(it)
                }
        }
}