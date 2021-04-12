package org.ebolapp.features.debug

import org.ebolapp.features.debug.usecases.*
import org.ebolapp.features.debug.usecases.GetMapRegionLegendDbgInfoUseCaseImpl
import org.ebolapp.db.DatabaseWrapper

class DebugUseCaseFactory(
    private val databaseWrapper: DatabaseWrapper
) {
    
    fun createCasesDebugInfoUseCase() : GetMapRegionCasesDbgInfoUseCase =
        GetMapRegionCasesDbgInfoUseCaseImpl(databaseWrapper)

    fun createStatesCasesDebugInfoUseCase() : GetMapStatesCasesDbgInfoUseCase =
        GetMapStatesCasesDbgInfoUseCaseImpl(databaseWrapper)

    fun createCasesLegendDebugInfoUseCase() : GetMapRegionLegendDbgInfoUseCase =
        GetMapRegionLegendDbgInfoUseCaseImpl(databaseWrapper)
    
    fun createMapRegionsDebugInfoUseCase() : GetMapRegionsDbgInfoUseCase = 
        GetMapRegionsDbgInfoUseCaseImpl(databaseWrapper)
    
    fun createUserLocationsDebugInfoUseCase() : GetUserLocationsDbgInfoUseCase =
        GetUserLocationsDbgInfoUseCaseImpl(databaseWrapper)
    
    fun createVisitsDebugInfoUseCase(): GetVisitsDbgInfoUseCase =
        GetVisitDbgInfoUseCaseImpl(databaseWrapper)
    
    fun createRiskMatchesDebugInfoUseCase() : GetRiskMatchDbgInfoUseCase = 
        GetRiskMatchDbgInfoUseCaseImpl(databaseWrapper)
    
    fun createRiskMatchNotificationsDebugInfoUseCase() : GetRiskMatchNotificationDbgInfoUseCase =
        GetRiskMatchNotificationDbgInfoUseCaseImpl(databaseWrapper)
    
}