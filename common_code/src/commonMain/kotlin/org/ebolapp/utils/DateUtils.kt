package org.ebolapp.utils

import kotlinx.datetime.*
import kotlin.math.abs

object DateUtils {

    enum class TZone {
        CURRENT,
        UTC
    }

    private fun TZone.toDateTimeZone() : TimeZone =
        when(this) {
            TZone.CURRENT -> TimeZone.currentSystemDefault()
            TZone.UTC -> TimeZone.UTC
        }

    private val addZero: (Int) -> String = {
        if (it > 9) "$it" else "0$it"
    }

    fun daysBetween(timestampFromSec: Long, timestampToSec: Long) : Int {
        return abs(timestampToSec - timestampFromSec).toInt()/60/60/24
    }

    fun formattedTime(timestampSec: Long, timeZone: TZone = TZone.CURRENT) : String {
        val date = Instant
            .fromEpochSeconds(timestampSec)
            .toLocalDateTime(timeZone.toDateTimeZone())
        return "${addZero(date.hour)}:${addZero(date.minute)}"
    }

    fun formattedDate(timestampSec: Long, timeZone: TZone = TZone.CURRENT) : String {
        val date = Instant
            .fromEpochSeconds(timestampSec)
            .toLocalDateTime(timeZone.toDateTimeZone())
        return "${date.month.name} ${date.dayOfMonth}, ${date.year}"
    }

    fun pointFormattedDate(timestampSec: Long, timeZone: TZone = TZone.CURRENT) : String {
        val date = Instant
            .fromEpochSeconds(timestampSec)
            .toLocalDateTime(timeZone.toDateTimeZone())
        return "${addZero(date.dayOfMonth)}.${addZero(date.monthNumber)}.${date.year}"
    }

    fun formattedPeriodBetween(timestampStartSec: Long, timestampEndSec: Long, timeZone: TZone = TZone.CURRENT) : String {
        val dateStart = Instant.fromEpochSeconds(timestampStartSec).toLocalDateTime(timeZone.toDateTimeZone())
        val dateEnd = Instant.fromEpochSeconds(timestampEndSec).toLocalDateTime(timeZone.toDateTimeZone())
        return if (dateStart.year == dateEnd.year) {
            // The same year
            if (dateStart.monthNumber == dateEnd.monthNumber) {
                // The same month
                if (dateStart.dayOfMonth == dateEnd.dayOfMonth) {
                    // The same day
                    if (dateStart.hour == dateEnd.hour && dateStart.minute == dateEnd.minute) {
                        // HH:mm, dd.MM.yyyy
                        "${formattedTime(timestampStartSec, timeZone)}, ${pointFormattedDate(timestampEndSec, timeZone)}"
                    } else {
                        // HH:mm - HH:mm, dd.MM.yyyy
                        "${formattedTime(timestampStartSec, timeZone)} - ${formattedTime(timestampEndSec, timeZone)}, ${pointFormattedDate(timestampEndSec, timeZone)}"
                    }
                } else {
                    // dd - dd.MM.yyyy
                    "${addZero(dateStart.dayOfMonth)} - ${pointFormattedDate(timestampEndSec, timeZone)}"
                }
            } else {
                // dd.MM - dd.MM.yyyy
                "${addZero(dateStart.dayOfMonth)}.${addZero(dateStart.monthNumber)} - ${pointFormattedDate(timestampEndSec, timeZone)}"
            }
        } else {
            // dd.MM.yyyy - dd.MM.yyyy
            "${pointFormattedDate(timestampStartSec, timeZone)} - ${pointFormattedDate(timestampEndSec, timeZone)}"
        }
    }

    fun dayStartTimestamp(timestampSec: Long = nowTimestampSec()): Long {
        return timestampAtStartOfDayByAddingDays(timestampSec, 0)
    }

    fun dayEndTimestamp(timestampSec: Long = nowTimestampSec()): Long {
        return timestampAtStartOfDayByAddingDays(timestampSec, 1) - 1
    }

    fun timestampAtStartOfDayByAddingDays(timestampSec: Long = Clock.System.now().epochSeconds, days: Int): Long {
        return Instant
            .fromEpochSeconds(timestampSec)
            .toLocalDateTime(TimeZone.UTC)
            .date.plus(DatePeriod(days = days))
            .atStartOfDayIn(TimeZone.UTC)
            .toEpochMilliseconds() / 1000
    }

    fun dateForURL(timestampSec: Long) : String {
        val date = Instant
            .fromEpochSeconds(timestampSec)
            .toLocalDateTime(TimeZone.UTC)
        return "${date.year}/${addZero(date.month.number)}/${addZero(date.dayOfMonth)}"
    }

    fun isToday(timestampSec: Long) : Boolean {
        val todayTimestamp = timestampAtStartOfDayByAddingDays(days = 0)
        val dayStartTimestamp = dayStartTimestamp(timestampSec)
        return todayTimestamp == dayStartTimestamp
    }

    fun nowTimestampSec() : Long {
        return Clock.System.now()
            .toLocalDateTime(TimeZone.UTC)
            .toInstant(TimeZone.UTC)
            .epochSeconds
    }

    fun splitTimestampsInDayIntervals(
        startTimestampSec: Long,
        endTimestampSec: Long
    ): List<DayInterval> {

        if (startTimestampSec >= endTimestampSec) return emptyList()

        var startTimestampSecCursor = startTimestampSec
        val result = mutableListOf<DayInterval>()

        var finished = false

        while (!finished) {

            val endTimestampSecCursor =
                if (endTimestampSec <= dayEndTimestamp(startTimestampSecCursor)) {
                    finished = true
                    endTimestampSec
                } else {
                    dayEndTimestamp(startTimestampSecCursor)
                }

            result.add(
                DayInterval(
                    startTimestampSec = startTimestampSecCursor,
                    endTimestampSec = endTimestampSecCursor
                )
            )

            startTimestampSecCursor = dayStartTimestamp(endTimestampSecCursor + 1)
        }
        return result
    }

    data class DayInterval(
        val startTimestampSec: Long,
        val endTimestampSec: Long
    )
}

fun Long.format(): String {
    return Instant
        .fromEpochSeconds(this)
        .toLocalDateTime(TimeZone.UTC)
        .toString()
}