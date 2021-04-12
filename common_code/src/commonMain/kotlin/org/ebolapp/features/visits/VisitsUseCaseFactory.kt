package org.ebolapp.features.visits


import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.db.RiskMatchDbApiImpl
import org.ebolapp.features.visits.db.UserLocationsDbApi
import org.ebolapp.features.visits.db.UserLocationsDbApiImpl
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.db.VisitsDbApiImpl
import org.ebolapp.features.visits.usecases.*
import org.ebolapp.features.visits.usecases.locations.*
import org.ebolapp.db.DatabaseWrapper

class VisitsUseCaseFactory(databaseWrapper: DatabaseWrapper) {

    private val riskMatchDbApi: RiskMatchDbApi = RiskMatchDbApiImpl(databaseWrapper)
    private val userLocationsDbApi : UserLocationsDbApi = UserLocationsDbApiImpl(databaseWrapper)
    private val visitsDbApi : VisitsDbApi = VisitsDbApiImpl(databaseWrapper)

    // User Location
    fun createSaveUserLocationsUseCase() : SaveUserLocationsUseCase =
        SaveUserLocationsUseCaseImpl(userLocationsDbApi, createUpdateVisitsUseCase())

    fun createGetUserLocationsUseCase() : GetUserLocationsForDayByTimestampUseCase =
        GetUserLocationsForDayByTimestampUseCaseImpl(userLocationsDbApi)

    fun createGetLastUserLocation() : GetLastUserLocationUseCase =
        GetLastUserLocationUseCaseImpl(userLocationsDbApi)

    // Visits
    fun createGetVisitsBetweenTimestampsUseCase() : GetVisitsBetweenTimestampsUseCase =
        GetVisitsBetweenTimestampsUseCaseImpl(visitsDbApi)

    fun createObserveVisitsUseCase() : ObserveVisitsUseCase =
        ObserveVisitsUseCaseImpl(visitsDbApi)

    fun createUpdateVisitsUseCase() : UpdateVisitsUseCase =
        UpdateVisitsUseCaseImpl(visitsDbApi)

    fun createGetVisitsForDayUseCase() : GetVisitsForDayUseCase =
        GetVisitsForDayUseCaseImpl(visitsDbApi)

    fun debugCreateRecalculateVisitsUseCase() : DebugRecalculateVisitsUseCase =
        DebugRecalculateVisitsUseCaseImpl(
            getUserLocationsForDayByTimestampUseCase = createGetUserLocationsUseCase(),
            updateVisitsUseCase = createUpdateVisitsUseCase(),
            riskMatchDbApi = riskMatchDbApi,
            visitsDbApi = visitsDbApi
        )

}