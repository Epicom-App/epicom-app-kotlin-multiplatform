package org.ebolapp.features.riskmatching

import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.features.cases.network.Endpoints
import org.ebolapp.features.regions.MapRegionsUseCaseFactory
import org.ebolapp.features.riskmatching.db.RiskMatchDbApi
import org.ebolapp.features.riskmatching.db.RiskMatchDbApiImpl
import org.ebolapp.features.riskmatching.usecase.*
import org.ebolapp.features.riskmatching.usecase.GetRiskMatchesForDayUseCaseImpl
import org.ebolapp.features.visits.VisitsUseCaseFactory
import org.ebolapp.features.visits.db.VisitsDbApi
import org.ebolapp.features.visits.db.VisitsDbApiImpl

class RiskMatchingUseCaseFactory (
    private val databaseWrapper: DatabaseWrapper,
    private val endpoints: Endpoints
) {

    private val mapRegionsUseCaseFactory: MapRegionsUseCaseFactory =
        MapRegionsUseCaseFactory(databaseWrapper, endpoints)

    private val visitsUseCaseFactory: VisitsUseCaseFactory =
        VisitsUseCaseFactory(databaseWrapper)

    private val riskMatchDbApi: RiskMatchDbApi = RiskMatchDbApiImpl(databaseWrapper)
    private val visitsDbApi: VisitsDbApi = VisitsDbApiImpl(databaseWrapper)

    fun createPerformRiskMatchingForDayUseCase() : PerformRiskMatchingForDayUseCase =
        PerformRiskMatchingForDayUseCaseImpl(
            riskMatchDbApi = riskMatchDbApi,
            getVisitsBetweenTimestampsUseCase =
                visitsUseCaseFactory.createGetVisitsBetweenTimestampsUseCase(),
            getMapRegionIdByPositionUseCase =
                mapRegionsUseCaseFactory.createGetMapRegionByPositionUseCase(),
            getMapRegionInfoUseCase =
                mapRegionsUseCaseFactory.createGetMapRegionInfoUseCase(),
            cleanRiskMatchesCacheUseCase =
                createCleanRiskMatchesCacheUseCase(),
            createRiskMatchNotificationUseCase =
                createCreateRiskMatchNotificationUseCase()
        )

    fun createCreateRiskMatchNotificationUseCase() =
        CreateRiskMatchNotificationUseCaseImpl(
            riskMatchDbApi = riskMatchDbApi,
            createAnalyseRiskMatchUseCase()
        )

    fun createAnalyseRiskMatchUseCase() =
        AnalyseRiskMatchUseCaseImpl(riskMatchDbApi, visitsDbApi)

    fun createCleanRiskMatchesCacheUseCase() : CleanRiskMatchesCacheUseCase =
        CleanRiskMatchesCacheUseCaseImpl(riskMatchDbApi = riskMatchDbApi)

    fun createGetRiskMatchesForDayUseCase() : GetRiskMatchesForDayUseCase =
        GetRiskMatchesForDayUseCaseImpl(
            riskMatchDbApi = riskMatchDbApi,
            performRiskMatchingForDayUseCase = createPerformRiskMatchingForDayUseCase()
        )

    fun createObserveRiskMatchesForTodayUseCase() : ObserveRiskMatchesForTodayUseCase =
        ObserveRiskMatchesForTodayUseCaseImpl(
            observeVisitsUseCase = visitsUseCaseFactory.createObserveVisitsUseCase(),
            performRiskMatchingForDayUseCase = createPerformRiskMatchingForDayUseCase(),
            getRiskMatchesForDayUseCase = createGetRiskMatchesForDayUseCase()
        )

    fun createObserveRiskMatchesForPeriodUseCase() : ObserveRiskMatchForPeriodUseCase =
        ObserveRiskMatchForPeriodUseCaseImpl(
            getRiskMatchesForDayUseCase = createGetRiskMatchesForDayUseCase(),
            observeRiskMatchesForTodayUseCase = createObserveRiskMatchesForTodayUseCase()
        )

    fun createObserveRiskDatesOffsetsFromCurrentDateForPeriod() : ObserveRiskDatesOffsetsFromCurrentDateForPeriod =
        ObserveRiskDatesOffsetsFromCurrentDateForPeriodImpl(
            observeRiskMatchForPeriodUseCase = createObserveRiskMatchesForPeriodUseCase()
        )

    fun createGetRiskMatchesForPeriodUseCase() : GetRiskMatchesForPeriodUseCase =
        GetRiskMatchesForPeriodUseCaseImpl(
            getRiskMatchesForDayUseCase = createGetRiskMatchesForDayUseCase()
        )

    fun createGetNotRiskyVisitsForDayUseCase(): GetNotRiskyVisitsForDayUseCase =
        GetNotRiskyVisitsForDayUseCaseImpl(
            getRiskMatchesForDayUseCase = createGetRiskMatchesForDayUseCase(),
            getVisitsForDayUseCase = visitsUseCaseFactory.createGetVisitsForDayUseCase()
        )

    fun createGetNotNotifiedRiskMatchesUseCase(): GetNotNotifiedRiskMatchesUseCase =
        GetNotNotifiedRiskMatchesUseCaseImpl(
            riskMatchDbApi = riskMatchDbApi,
            performRiskMatchingForDayUseCase = createPerformRiskMatchingForDayUseCase(),
            analyseRiskMatchUseCase = createAnalyseRiskMatchUseCase()
        )

    fun createObserveNotNotifiedRiskMatchesUseCase(): ObserveNotNotifiedRiskMatchesUseCase =
        ObserveNotNotifiedRiskMatchesUseCaseImpl(riskMatchDbApi = riskMatchDbApi)

    fun createMarkAsNotifiedRiskMatchesUseCase(): MarkAsNotifiedRiskMatchesUseCase =
        MarkAsNotifiedRiskMatchesUseCaseImpl(riskMatchDbApi)

}
