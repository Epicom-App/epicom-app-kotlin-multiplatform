package org.ebolapp.features.staticPages.network

import org.ebolapp.shared.network.NetworkApiParameter
import org.ebolapp.shared.network.NetworkApiParameterNames
import org.ebolapp.shared.network.NetworkParametersSendStrategy
import org.ebolapp.utils.Language

data class StaticPages(
    private val imprint: NetworkParametersSendStrategy,
    private val dataPrivacy: NetworkParametersSendStrategy,
    private val about: NetworkParametersSendStrategy
) {
    val imprintURL: String
        get() = imprint.getURL(listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.LANGUAGE,
                value = Language().code
            )
        ))

    val dataPrivacyURL: String
        get() = dataPrivacy.getURL(listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.LANGUAGE,
                value = Language().code
            )
        ))

    val aboutURL: String
        get() = about.getURL(listOf(
            NetworkApiParameter(
                name = NetworkApiParameterNames.LANGUAGE,
                value = Language().code
            )
        ))
}