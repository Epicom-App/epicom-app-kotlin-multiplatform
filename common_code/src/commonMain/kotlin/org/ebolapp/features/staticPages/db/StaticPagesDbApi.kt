package org.ebolapp.features.staticPages.db

import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.db.StaticPagesCacheTable
import org.ebolapp.utils.DateUtils

interface StaticPagesDbApi {
    suspend fun selectWithUrl(url: String) : StaticPagesCacheTable?
    suspend fun insertOrUpdate(url: String, content: String)
    suspend fun deleteWithUrl(url: String)
    suspend fun deleteAllBeforeTimestamp(timestampSec: Long)
}

internal class StaticPagesDbApiImpl(
    databaseWrapper: DatabaseWrapper
) : StaticPagesDbApi {

    private val staticPagesCacheTableQueries = databaseWrapper.database.staticPagesCacheTableQueries

    override suspend fun selectWithUrl(url: String): StaticPagesCacheTable? =
        staticPagesCacheTableQueries.selectWithUrl(url).executeAsOneOrNull()

    override suspend fun insertOrUpdate(url: String, content: String) {
        staticPagesCacheTableQueries.transaction {
            staticPagesCacheTableQueries.insertOrReplace(
                StaticPagesCacheTable(
                    url = url,
                    content = content,
                    timestampSec = DateUtils.nowTimestampSec()
                )
            )
        }
    }

    override suspend fun deleteWithUrl(url: String) {
        staticPagesCacheTableQueries.transaction {
            staticPagesCacheTableQueries.deleteWithUrl(url)
        }
    }

    override suspend fun deleteAllBeforeTimestamp(timestampSec: Long) {
        staticPagesCacheTableQueries.transaction {
            staticPagesCacheTableQueries.deleteAllBeforeTimestamp(timestampSec)
        }
    }
}