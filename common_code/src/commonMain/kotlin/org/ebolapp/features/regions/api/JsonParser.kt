package org.ebolapp.features.regions.api

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.ebolapp.features.regions.utils.JsonFileReader

interface JsonStatesParser {
    suspend fun parseStates(): List<StateRegion>
}

interface JsonDistrictsParser {
    suspend fun parseDistricts(): List<DistrictRegion>
}

internal class JsonParserImpl(
    private val fileReader: JsonFileReader
) : JsonStatesParser, JsonDistrictsParser {

    override suspend fun parseStates(): List<StateRegion> {
        return withContext(Dispatchers.Default) {
            Json.decodeFromString(fileReader.get())
        }
    }

    override suspend fun parseDistricts(): List<DistrictRegion> {
        return withContext(Dispatchers.Default) {
            Json.decodeFromString(fileReader.get())
        }
    }
}
