package org.ebolapp.features.staticPages

import org.ebolapp.db.DatabaseWrapper
import org.ebolapp.features.staticPages.db.StaticPagesDbApiImpl
import org.ebolapp.features.staticPages.network.StaticPagesNetworkApiImpl
import org.ebolapp.features.staticPages.usecases.GetStaticPageUseCase
import org.ebolapp.features.staticPages.usecases.GetStaticPageUseCaseImpl
import org.ebolapp.shared.network.AppHttpClientImpl

class StaticPagesUseCaseFactory(
    private val databaseWrapper: DatabaseWrapper
) {

    private val httpClient = AppHttpClientImpl().getClient()
    private val staticPagesNetworkApi = StaticPagesNetworkApiImpl(httpClient)

    fun createGetStaticPageUseCase() : GetStaticPageUseCase =
        GetStaticPageUseCaseImpl(staticPagesNetworkApi, StaticPagesDbApiImpl(databaseWrapper))
}