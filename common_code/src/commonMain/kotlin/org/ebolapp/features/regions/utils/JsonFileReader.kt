package org.ebolapp.features.regions.utils

interface JsonFileReader {
    suspend fun get(): String
}