package org.ebolapp.db.adapters

import com.squareup.sqldelight.ColumnAdapter
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ebolapp.features.visits.entities.Visit

val visitAdapter = object : ColumnAdapter<Visit, String> {

    override fun decode(databaseValue: String): Visit {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: Visit): String {
        return Json.encodeToString(value)
    }
}