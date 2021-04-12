object GradleBuildPlugins {

    object Jacoco {
        const val version = "0.15.0"
        const val classPath = "com.vanniktech:gradle-android-junit-jacoco-plugin:$version"
        const val plugin = "com.vanniktech.android.junit.jacoco"
    }

    object Detekt {
        const val version = "1.10.0"
        const val plugin = "io.gitlab.arturbosch.detekt"
        const val formatting = "io.gitlab.arturbosch.detekt:detekt-formatting:$version"

    }

    object BuildSrcVersions {
        const val plugin = "de.fayard.buildSrcVersions"
        const val version = "0.7.0"
    }

    object Versions {
        const val gradle = "4.1.3"
        const val afresgen = "1.2.2"
        const val navigation = "2.3.0"
        const val appBadge = "1.0.3"
        const val firebase = "4.3.3"
    }

    const val jacoco = "${Jacoco.plugin}:${Jacoco.version}"
    const val gradle = "com.android.tools.build:gradle:${Versions.gradle}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Libs.Kotlin.kotlinVersion}"
    const val navigation =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val afResGen = "de.appsfactory:res-gen:${Versions.afresgen}"

    //const val versionUpdates = "${BuildSrcVersions.plugin}:${BuildSrcVersions.version}"
    const val appBadge = "gradle.plugin.App-Badge:plugin:${Versions.appBadge}"
    const val firebase = "com.google.gms:google-services:${Versions.firebase}"
    const val koin = "org.koin:koin-gradle-plugin:${Libs.Koin.version}"
    const val kotlinSerialization = "org.jetbrains.kotlin:kotlin-serialization:${Libs.Kotlin.kotlinVersion}"
    const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:1.4.4"
}