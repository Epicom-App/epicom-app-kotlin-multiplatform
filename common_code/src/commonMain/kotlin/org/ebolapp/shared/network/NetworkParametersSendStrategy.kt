package org.ebolapp.shared.network

interface NetworkParametersSendStrategy {
    fun getURL(parameters: List<NetworkApiParameter> = emptyList()) : String
}

class NoParametersStrategy(
    private val endpoint: String,
) : NetworkParametersSendStrategy {

    override fun getURL(parameters: List<NetworkApiParameter>): String {
        return endpoint
    }
}

class TemplateParametersStrategy(
    private val endpoint: String,
    private val templates: Map<NetworkApiParameterNames, String>
) : NetworkParametersSendStrategy {

    override fun getURL(parameters: List<NetworkApiParameter>): String {
        var endpoint = this.endpoint
        parameters.forEach {
            templates[it.name]?.apply {
                endpoint = endpoint.replace(this, it.value.toString())
            }
        }
        return endpoint
    }
}

enum class NetworkApiParameterNames(val parameter: String) {
    TIMESTAMP("timestamp"),
    LANGUAGE("language")
}

class NetworkApiParameter(val name: NetworkApiParameterNames, val value: Any)