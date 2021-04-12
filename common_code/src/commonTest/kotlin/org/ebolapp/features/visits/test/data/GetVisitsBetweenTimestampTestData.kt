package org.ebolapp.features.visits.test.data

import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit

object GetVisitsBetweenTimestampTestData {


    object OneVisitStartsBeforeRequestedInterval {
        const val startDayTimestamp = 1605571200L // 11/16/2020 @ 6:00pm (UTC) - The day before interval
        const val endDayTimestamp = 1605657599L // 11/17/2020 @ 11:59pm (UTC)

        const val visitOneStartTimestamp = 1605549600L // 11/17/2020 @ 7:00am (UTC)
        const val visitOneEndTimestamp = 1605607200L // 11/17/2020 @ 10:00:00 am (UTC)
        val visitOne = Visit(
            id = 0,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitOneStartTimestamp,
            endTimestamp = visitOneEndTimestamp
        )

        const val visitTwoStartTimestamp = 1605607201L // 11/17/2020 @ 10:00:01 am (UTC)
        const val visitTwoEndTimestamp = 1605614400L // 11/17/2020 @ 12:00:00 pm (UTC)
        val visitTwo = Visit(
            id = 1,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitTwoStartTimestamp,
            endTimestamp = visitTwoEndTimestamp
        )

        const val visitThreeStartTimestamp = 1605614401L  // 11/17/2020 @ 12:00:01 pm (UTC)
        const val visitThreeEndTimestamp = 1605639600L // 11/17/2020 @ 7:00pm (UTC)
        val visitThree = Visit(
            id = 2,
            position = Position(lat = 0.0, lon = 0.0),
            startTimestamp = visitThreeStartTimestamp,
            endTimestamp = visitThreeEndTimestamp
        )
    }

    object AllVisitsAreInsideTheInterval {

        const val startDayTimestamp = 1605571200L // 11/17/2020 @ 12:00am (UTC)
        const val endDayTimestamp = 1605657599L // 11/17/2020 @ 11:59pm (UTC)

        const val visitOneStartTimestamp = 1605596400L // 11/17/2020 @ 7:00am (UTC)
        const val visitOneEndTimestamp = 1605607200L // 11/17/2020 @ 10:00:00 am (UTC)
        val visitOne = Visit(
            id = 0,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitOneStartTimestamp,
            endTimestamp = visitOneEndTimestamp
        )

        const val visitTwoStartTimestamp = 1605607201L // 11/17/2020 @ 10:00:01 am (UTC)
        const val visitTwoEndTimestamp = 1605614400L // 11/17/2020 @ 12:00:00 pm (UTC)
        val visitTwo = Visit(
            id = 1,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitTwoStartTimestamp,
            endTimestamp = visitTwoEndTimestamp
        )

        const val visitThreeStartTimestamp = 1605614401L  // 11/17/2020 @ 12:00:01 pm (UTC)
        const val visitThreeEndTimestamp = 1605639600L // 11/17/2020 @ 7:00pm (UTC)
        val visitThree = Visit(
            id = 2,
            position = Position(lat = 0.0, lon = 0.0),
            startTimestamp = visitThreeStartTimestamp,
            endTimestamp = visitThreeEndTimestamp
        )
    }

    object OneVisitEndsAfterRequestedInterval {

        const val startDayTimestamp = 1605571200L // 11/17/2020 @ 12:00am (UTC)
        const val endDayTimestamp = 1605657599L // 11/17/2020 @ 11:59pm (UTC)

        const val visitOneStartTimestamp = 1605596400L // 11/17/2020 @ 7:00am (UTC)
        const val visitOneEndTimestamp = 1605607200L // 11/17/2020 @ 10:00:00 am (UTC)
        val visitOne = Visit(
            id = 0,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitOneStartTimestamp,
            endTimestamp = visitOneEndTimestamp
        )

        const val visitTwoStartTimestamp = 1605607201L // 11/17/2020 @ 10:00:01 am (UTC)
        const val visitTwoEndTimestamp = 1605614400L // 11/17/2020 @ 12:00:00 pm (UTC)
        val visitTwo = Visit(
            id = 1,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitTwoStartTimestamp,
            endTimestamp = visitTwoEndTimestamp
        )

        const val visitThreeStartTimestamp = 1605614401L  // 11/17/2020 @ 12:00:01 pm (UTC)
        const val visitThreeEndTimestamp = 1605679200L // 11/18/2020 @ 6:00am (UTC) - The day after interval
        val visitThree = Visit(
            id = 2,
            position = Position(lat = 0.0, lon = 0.0),
            startTimestamp = visitThreeStartTimestamp,
            endTimestamp = visitThreeEndTimestamp
        )
    }

    object VisitStartsBeforeAndEndsAfterInterval {

        const val startDayTimestamp = 1605571200L // 11/17/2020 @ 12:00am (UTC)
        const val endDayTimestamp = 1605657599L // 11/17/2020 @ 11:59pm (UTC)

        const val visitOneStartTimestamp = 1605499200L // 11/16/2020 @ 4:00am (UTC) - The day before interval
        const val visitOneEndTimestamp = 1605686400L // 11/18/2020 @ 8:00am (UTC) - The day after interval
        val visitOne = Visit(
            id = 0,
            position = Position(lat = 0.0, lon = 0.0), // Irrelevant for the test
            startTimestamp = visitOneStartTimestamp,
            endTimestamp = visitOneEndTimestamp
        )
    }
}