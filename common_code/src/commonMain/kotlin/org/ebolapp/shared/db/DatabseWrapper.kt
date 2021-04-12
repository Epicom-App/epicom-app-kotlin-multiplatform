package org.ebolapp.db

import org.ebolapp.db.adapters.visitAdapter

class DatabaseWrapper (
    databaseDriverFactory: DatabaseDriverFactory
) {
    val database = AppDatabase(
        databaseDriverFactory.createDriver().also { driver ->
            driver.execute(null, "PRAGMA foreign_keys=ON", 0)
        },
        RiskMatchTable.Adapter(visitAdapter)
    )
}