package org.ebolapp.features.staticPages.usecases

import org.ebolapp.features.staticPages.db.StaticPagesDbApi
import org.ebolapp.features.staticPages.network.StaticPagesNetworkApi

interface GetStaticPageUseCase {
    @Throws(Throwable::class)
    suspend operator fun invoke(url: String) : String
}

class GetStaticPageUseCaseImpl(
    private val networkApi: StaticPagesNetworkApi,
    private val staticPagesDbApi: StaticPagesDbApi
) : GetStaticPageUseCase {

    @Throws(Throwable::class)
    override suspend fun invoke(url: String): String =
        try {
            networkApi.getHTML(url).also {
                staticPagesDbApi.insertOrUpdate(url, it)
            }
        } catch (exc: Throwable) {
            staticPagesDbApi.selectWithUrl(url)?.content ?: throw exc
        }
}