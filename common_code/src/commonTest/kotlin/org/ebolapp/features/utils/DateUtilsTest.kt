package org.ebolapp.features.utils

import org.ebolapp.utils.DateUtils
import kotlin.test.Test
import kotlin.test.assertTrue

class DateUtilsTest {

    @Test
    fun testTimestampSplitDifferentDays() {

        val startTimestampSec = 1605621600L // 11/17/2020 @ 2:00pm
        val endTimestampSec = 1605970800L // 11/21/2020 @ 3:00pm

        // Should result in 5 entries, 1st and last should not be a whole day
        val result = DateUtils.splitTimestampsInDayIntervals(
            startTimestampSec,
            endTimestampSec
        )

        assertTrue { result.size == 5 } // check we have 5 days as planned

        assertTrue { result.first().startTimestampSec == startTimestampSec }
        assertTrue { result.first().endTimestampSec == DateUtils.dayEndTimestamp(startTimestampSec) }

        assertTrue { result.last().startTimestampSec == DateUtils.dayStartTimestamp(endTimestampSec) }
        assertTrue { result.last().endTimestampSec == endTimestampSec }

    }

    @Test
    fun testTimestampSplitSameDay() {

        val startTimestampSec = 1605574800L // 11/17/2020 @ 1:00am
        val endTimestampSec = 1605636000L // 11/17/2020 @ 18:00am

        val result = DateUtils.splitTimestampsInDayIntervals(
            startTimestampSec,
            endTimestampSec
        )

        assertTrue { result.size == 1 }

        assertTrue { result.first().startTimestampSec == startTimestampSec }
        assertTrue { result.first().endTimestampSec == endTimestampSec }

    }

    @Test
    fun testPeriodDateFormat() {

        // Case 1: Different year
        val startTimestampSec1 = 1607935744L // Mon Dec 14 2020 08:49:04 GMT+0000
        val endTimestampSec1 = 1615888144L // Tue Mar 16 2021 09:49:04 GMT+0000

        val result1 = DateUtils.formattedPeriodBetween(
            startTimestampSec1,
            endTimestampSec1,
            DateUtils.TZone.UTC
        )

        assertTrue { result1 == "14.12.2020 - 16.03.2021" }

        // Case 2: Different month
        val startTimestampSec2 = 1613292544L // Sun Feb 14 2021 08:49:04 GMT+0000
        val endTimestampSec2 = 1615888144L // Tue Mar 16 2021 09:49:04 GMT+0000

        val result2 = DateUtils.formattedPeriodBetween(
            startTimestampSec2,
            endTimestampSec2,
            DateUtils.TZone.UTC
        )

        assertTrue { result2 == "14.02 - 16.03.2021" }

        // Case 3: Different day of month
        val startTimestampSec3 = 1615708144L // Sun Mar 14 2021 07:49:04 GMT+0000
        val endTimestampSec3 = 1615888144L // Tue Mar 16 2021 09:49:04 GMT+0000

        val result3 = DateUtils.formattedPeriodBetween(
            startTimestampSec3,
            endTimestampSec3,
            DateUtils.TZone.UTC
        )

        assertTrue { result3 == "14 - 16.03.2021" }

        // Case 4: Different time
        val startTimestampSec4 = 1615880944L // Tue Mar 16 2021 07:49:04 GMT+0000
        val endTimestampSec4 = 1615888144L // Tue Mar 16 2021 09:49:04 GMT+0000

        val result4 = DateUtils.formattedPeriodBetween(
            startTimestampSec4,
            endTimestampSec4,
            DateUtils.TZone.UTC
        )

        assertTrue { result4 == "07:49 - 09:49, 16.03.2021" }

        // Case 5: Different seconds
        val startTimestampSec5 = 1615888142L // Tue Mar 16 2021 09:49:02 GMT+0000
        val endTimestampSec5 = 1615888144L // Tue Mar 16 2021 09:49:04 GMT+0000

        val result5 = DateUtils.formattedPeriodBetween(
            startTimestampSec5,
            endTimestampSec5,
            DateUtils.TZone.UTC
        )

        assertTrue { result5 == "09:49, 16.03.2021" }
    }
    
}