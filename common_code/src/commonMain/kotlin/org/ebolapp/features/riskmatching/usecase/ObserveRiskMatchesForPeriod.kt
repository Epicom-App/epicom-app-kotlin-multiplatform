package org.ebolapp.features.riskmatching.usecase

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import org.ebolapp.features.riskmatching.entities.RiskMatch
import org.ebolapp.utils.DateUtils
import kotlin.native.concurrent.ThreadLocal

interface ObserveRiskMatchForPeriodUseCase {
    operator fun invoke(daysPeriod: Int) : Flow<List<RiskMatch>>
    fun observe(days: Int, onUpdate: (List<RiskMatch>) -> Unit) : Job
}

internal class ObserveRiskMatchForPeriodUseCaseImpl(
    private val getRiskMatchesForDayUseCase: GetRiskMatchesForDayUseCase,
    private val observeRiskMatchesForTodayUseCase: ObserveRiskMatchesForTodayUseCase
): ObserveRiskMatchForPeriodUseCase {

    @ThreadLocal
    companion object {
        private var attemptsCount = emptyMap<Long, Long>().toMutableMap()
        private fun calculateDelayDuration(timestamp: Long) : Long {
            val attempt = (attemptsCount[timestamp] ?: 0L) + 1
            val possibleCount = 6L
            return if (attempt < possibleCount) {
                // if attempts count < possibleCount increase delay between attempts by 10 sec each time
                attemptsCount[timestamp] = attempt
                (attempt % possibleCount) * 10 * 1000
            } else {
                // if attempts count = possibleCount return 1 hour delay and drop attempts count
                attemptsCount[timestamp] = 0L
                60*60*1000
            }
        }
    }

    @ExperimentalCoroutinesApi
    override fun invoke(daysPeriod: Int): Flow<List<RiskMatch>> = channelFlow {
        val todayTimestamp = DateUtils.dayStartTimestamp(DateUtils.nowTimestampSec())
        handleRetries(observeRiskMatchesForTodayUseCase(), todayTimestamp).onEach { send(it) }.launchIn(this)
        createListOfFlowsForPrevDays(daysPeriod).forEach { flow ->
            flow.onEach { send(it) }.launchIn(this)
        }
    }

    @ExperimentalCoroutinesApi
    override fun observe(days: Int, onUpdate: (List<RiskMatch>) -> Unit) : Job =
        GlobalScope.launch {
            invoke(days)
                .flowOn(Dispatchers.Main)
                .collectLatest { matches ->
                    onUpdate(matches)
                }
        }

    private fun createListOfFlowsForPrevDays(daysPeriod: Int) : List<Flow<List<RiskMatch>>> =
        (daysPeriod downTo 1)
            .map { DateUtils.timestampAtStartOfDayByAddingDays(days = -it) }
            .map { handleRetries(flow { emit(getRiskMatchesForDayUseCase(it)) }, it) }


    private fun handleRetries(flow: Flow<List<RiskMatch>>, timestamp: Long) : Flow<List<RiskMatch>> =
        flow.retry {
            val duration = calculateDelayDuration(timestamp)
            println("Schedule next update for $timestamp in $duration")
            delay(duration)
            return@retry true
        }
}