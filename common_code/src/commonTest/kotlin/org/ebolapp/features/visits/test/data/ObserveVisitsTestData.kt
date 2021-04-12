package org.ebolapp.features.visits.test.data

import org.ebolapp.features.regions.entities.Position
import org.ebolapp.features.visits.entities.Visit

object ObserveVisitsTestData {

    object Basic {

        val firstVisitsBatch = listOf(
            Visit(
                id = 0,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 1,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 2,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            )
        )

        val secondVisitsBatch = listOf(
            Visit(
                id = 3,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 4,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 5,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            )
        )

        val thirdVisitsBatch = listOf(
            Visit(
                id = 6,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 7,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            ),
            Visit(
                id = 8,
                position = Position(0.0,0.0),
                startTimestamp = 0L,
                endTimestamp = 900L
            )
        )
    }

}