package org.ebolapp.shared.network

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

interface AppHttpClient {
    fun getClient(): HttpClient
}

internal class AppHttpClientImpl : AppHttpClient {

    override fun getClient(): HttpClient {
        return HttpClient {
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }
        }
    }
}

internal class MockAppHttpClientImpl(
    private val requestHandler: suspend MockRequestHandleScope.(request: HttpRequestData) -> HttpResponseData
) : AppHttpClient {

    override fun getClient(): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    requestHandler(request)
                }
            }
            install(JsonFeature) {
                val json = kotlinx.serialization.json.Json { ignoreUnknownKeys = true }
                serializer = KotlinxSerializer(json)
            }
        }
    }
}