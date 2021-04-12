object Libs {

    object Kotlin {

        const val kotlinVersion = "1.4.32"
        private const val dateTimeVersion = "0.1.1"

        const val stdLib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
        const val dateTime = "org.jetbrains.kotlinx:kotlinx-datetime:${dateTimeVersion}"

        object Coroutines {
            private const val coroutinesVersion = "1.4.2"
            private const val coroutinesNativeMtVersion = "1.4.2-native-mt"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val playServices = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:$coroutinesVersion"
            const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val coreMt = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesNativeMtVersion"
        }
    }

    object Koin {
        const val version = "2.2.0"
        const val core = "org.koin:koin-core:$version"
        const val coreExt = "org.koin:koin-core-ext:$version"
        const val android = "org.koin:koin-android:$version"
        const val androidxExt = "org.koin:koin-androidx-ext:$version"
        const val androidxViewModel = "org.koin:koin-androidx-viewmodel:$version"
        const val androidxViewModelScope = "org.koin:koin-androidx-scope:$version"
        const val androidxFragment = "org.koin:koin-androidx-fragment:$version"
        const val test = "org.koin:koin-test:$version"
    }

    object AndroidX {

        object Versions {
            const val appCompat = "1.1.0"
            const val coreKtx = "1.3.0"
            const val fragmentKtx = "1.2.4"
            const val workManager = "2.4.0"
        }

        const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
        const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
        const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"

        object Lifecycle {

            object Versions {
                const val lifecycle = "2.2.0"
                const val viewModelSavedState = "1.0.0"
            }

            const val extensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
            const val viewModelsSavedState =
                "androidx.lifecycle:lifecycle-viewmodel-savedstate:${Versions.viewModelSavedState}"
            const val liveData = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
            const val lifeCycle = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
            const val processLifecycle = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"
            const val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
            const val lifeCycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
        }

        object WorkManager {
            const val runtimeKtx = "androidx.work:work-runtime-ktx:${Versions.workManager}"
            const val testing = "androidx.work:work-testing:${Versions.workManager}"
        }

        private const val card_view_version ="1.0.0"
        const val cardView = "androidx.cardview:cardview:$card_view_version"
    }

    object SqlDelight {
        private const val version = "1.4.4"

        const val runtime = "com.squareup.sqldelight:runtime:${version}"
        const val driverAndroid = "com.squareup.sqldelight:android-driver:${version}"
        const val driveriOS = "com.squareup.sqldelight:native-driver:${version}"
    }

    object Layout {
        private const val flexBoxVersion = "2.0.1"
        private const val constraintVersion = "1.1.3"

        const val flexBox = "com.google.android:flexbox:${flexBoxVersion}"
        const val constraint = "androidx.constraintlayout:constraintlayout:${constraintVersion}"
    }

    object Material {
        const val version = "1.2.1"
        const val core = "com.google.android.material:material:$version"
    }

    object Glide {
        private const val version = "4.11.0"

        const val core = "com.github.bumptech.glide:glide:$version"
        const val coreApt = "com.github.bumptech.glide:compiler:$version"
    }

    object AfLogging {
        const val version = "0.3.0"

        const val core = "de.appsfactory.android:logger:$version"
    }

    object JsonParser {
        private const val version = "5.0.1"
        const val kotlinSerializationJson = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.0-RC2"
        const val kotlinSerializationCore = "org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC"
    }

    object NetworkingKtor {
        private const val ktor_version = "1.5.1"

        const val ktorCore = "io.ktor:ktor-client-core:$ktor_version"
        const val ktorSerialization = "io.ktor:ktor-client-serialization:$ktor_version"
        const val ktorMockEngine = "io.ktor:ktor-client-mock:$ktor_version"

        const val ktoriOS = "io.ktor:ktor-client-ios:$ktor_version"
        const val ktorMockEngineiOS = "io.ktor:ktor-client-mock-native:$ktor_version"

        const val ktorAndroid = "io.ktor:ktor-client-android:$ktor_version"
        const val ktorMockEngineAndroid = "io.ktor:ktor-client-mock-jvm:$ktor_version"
    }

    object JetPackNavigation {
        private const val version = "2.3.0"

        const val ui = "androidx.navigation:navigation-ui-ktx:$version"
        const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
        const val testing = "androidx.navigation:navigation-testing:$version"
    }

    object GooglePlay {
        private const val version = "17.0.0"

        const val location = "com.google.android.gms:play-services-location:${version}"
        const val map = "com.google.android.gms:play-services-maps:$version"
    }

    object Desugaring {
        private const val version = "1.0.10"

        const val core = "com.android.tools:desugar_jdk_libs:${version}"
    }

    object AppCenter {
        private const val version = "3.3.0"

        const val distributeNoOp = "com.microsoft.appcenter:appcenter-distribute-play:${version}"
        const val distribute = "com.microsoft.appcenter:appcenter-distribute:${version}"
        const val crashes = "com.microsoft.appcenter:appcenter-crashes:${version}"
    }

    object Moshi {
        const val version = "1.11.0"

        const val core = "com.squareup.moshi:moshi-kotlin:$version"
        const val coreAPT = "com.squareup.moshi:moshi-kotlin-codegen:$version"
        const val adapters = "com.squareup.moshi:moshi-adapters:$version"
    }

    object Conscrypt {
        private const val version = "2.4.0"

        const val core = "org.conscrypt:conscrypt-android:$version"
    }

    object CanaryLeak {
        const val version = "2.4"

        const val debugCore = "com.squareup.leakcanary:leakcanary-android:$version"
    }

    object FlowBindings {
        private const val flowbinding_version = "1.0.0-beta01"

        const val android = "io.github.reactivecircus.flowbinding:flowbinding-android:${flowbinding_version}"
        const val material = "io.github.reactivecircus.flowbinding:flowbinding-material:${flowbinding_version}"
        const val activity = "io.github.reactivecircus.flowbinding:flowbinding-activity:${flowbinding_version}"
        const val appCompat = "io.github.reactivecircus.flowbinding:flowbinding-appcompat:${flowbinding_version}"
        const val core = "io.github.reactivecircus.flowbinding:flowbinding-core:${flowbinding_version}"
        const val drawerLayout = "io.github.reactivecircus.flowbinding:flowbinding-drawerlayout:${flowbinding_version}"
        const val lifeCycle = "io.github.reactivecircus.flowbinding:flowbinding-lifecycle:${flowbinding_version}"
        const val navigation ="io.github.reactivecircus.flowbinding:flowbinding-navigation:${flowbinding_version}"
        const val preferences = "io.github.reactivecircus.flowbinding:flowbinding-preference:${flowbinding_version}"
        const val recyclerView = "io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${flowbinding_version}"
        const val swipeRefresh = "io.github.reactivecircus.flowbinding:flowbinding-swiperefreshlayout:${flowbinding_version}"
        const val viewPager = "io.github.reactivecircus.flowbinding:flowbinding-viewpager:${flowbinding_version}"
        const val viewPager2 = "io.github.reactivecircus.flowbinding:flowbinding-viewpager2:${flowbinding_version}"
    }

    object Test {

        private object Versions {
            const val junit4 = "4.13"
            const val junitExt = "1.1.1"
            const val testRunner = "1.2.0"
            const val espresso = "3.2.0"
            const val assertJ = "3.15.0"
            const val mockk = "1.9.3"
            const val conscript = "2.2.1"
        }

        const val kotlinTestCommon = "test-common"
        const val kotlinTestAnnotationsCommon = "test-annotations-common"

        const val kotlinTest = "test"
        const val kotlinTestJunit = "test-junit"

        const val junit4 = "junit:junit:${Versions.junit4}"
        const val junitExt = "androidx.test.ext:junit:${Versions.junitExt}"
        const val testRunner = "androidx.test:runner:${Versions.testRunner}"
        const val espresso = "androidx.test.espresso:espresso-core:${Versions.espresso}"
        const val assertJ = "org.assertj:assertj-core:${Versions.assertJ}"
        const val mockk = "io.mockk:mockk:${Versions.mockk}"
        const val conscript = "org.conscrypt:conscrypt-android:${Versions.conscript}"
    }
}
