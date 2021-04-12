package org.ebolapp.db

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}

fun DatabaseDriverFactory.dbName(): String  = "ebolapp.db"