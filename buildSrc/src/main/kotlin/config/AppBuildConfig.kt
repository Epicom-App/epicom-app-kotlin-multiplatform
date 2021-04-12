package config

object AppBuildConfig {

    object Debug {
        const val name = "debug"
        val manifestPlaceholders = mapOf<String,String>()
    }

    object Release {
        const val name = "release"
        val manifestPlaceholders = mapOf<String,String>()
    }

    /**
     * When Android BuildConfig variable it's used as string,
     * it should be quoted, because after generating the variable a level of quotes is removed.
     */
    fun quote(string: String): String {
        return "\"${string}\""
    }
}

enum class EndpointKey(val type: String) {
    BaseURL(type = "String"),
    FilesURL(type = "String")
}