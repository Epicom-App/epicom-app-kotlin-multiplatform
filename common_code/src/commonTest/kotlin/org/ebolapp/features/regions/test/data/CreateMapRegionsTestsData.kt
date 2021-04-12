package org.ebolapp.features.regions.test.data

import org.ebolapp.features.regions.utils.JsonFileReader

object CreateMapRegionsTestsData {

    object CreateMapRegionsUseCaseTestEmptyDbRegions {
        val mockStatesFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "id": "1",
            "name": "Schleswig-Holstein",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "id" : "2",
            "name":"Leipzig", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }]
    """.trimIndent()
            }
        }
        val mockDistrictsFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "bundeslandID": "1", 
            "areaName": "Flensburg", 
            "areaID": "01001", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "bundeslandID": "2", 
            "areaName": "Leipzig", 
            "areaID": "01002",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }] 
        """.trimIndent()
            }
        }
    }

    object CreateMapRegionsUseCaseTestAlreadyPopulatedDbRegions {
        val mockStatesFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "id": "1",
            "name": "Schleswig-Holstein",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "id" : "2",
            "name":"Leipzig", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }]
    """.trimIndent()
            }
        }
        val mockDistrictsFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "bundeslandID": "1", 
            "areaName": "Flensburg", 
            "areaID": "01001", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "bundeslandID": "2", 
            "areaName": "Leipzig", 
            "areaID": "01002",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }] 
        """.trimIndent()
            }
        }
    }

    object CreateMapRegionsUseCaseTestHalfPopulatedDbRegions {
        val mockStatesFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "id": "1",
            "name": "Schleswig-Holstein",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "id" : "2",
            "name":"Leipzig", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }]
    """.trimIndent()
            }
        }
        val mockDistrictsFileReader = object : JsonFileReader {
            override suspend fun get(): String {
                return """
        [{ 
            "bundeslandID": "1", 
            "areaName": "Flensburg", 
            "areaID": "01001", 
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        },
        { 
            "bundeslandID": "2", 
            "areaName": "Leipzig", 
            "areaID": "01002",
            "geoRing": [
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ],
                [ 
                    {"lon": 9.72694855217059, "lat": 53.5586543755165}, 
                    {"lon": 9.72953698118645, "lat": 53.5582212414827}, 
                    {"lon": 9.73031519620866, "lat": 53.5580193080416}
                ]
            ]
        }] 
        """.trimIndent()
            }
        }
    }


}