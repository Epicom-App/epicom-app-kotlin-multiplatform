package org.ebolapp.features.staticPages.network

import io.ktor.client.HttpClient
import io.ktor.client.request.*

interface StaticPagesNetworkApi {
    suspend fun getHTML(url: String): String
}

internal open class StaticPagesNetworkApiImpl(
    private val httpClient: HttpClient
) : StaticPagesNetworkApi {

    override suspend fun getHTML(url: String): String = httpClient.get(url)
}