import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
    id("kotlin-android-extensions")
    id("com.squareup.sqldelight")
}

repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}

sqldelight {
    database("AppDatabase") {
        packageName = "org.ebolapp.db"
        sourceFolders = listOf("sqldelight")
        deriveSchemaFromMigrations = true
        verifyMigrations = true
    }
}

kotlin {
    android()
    ios {
        binaries {
            framework {
                baseName = "CommonCode"
            }
        }
    }
    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(Libs.Kotlin.stdLib)
                implementation(Libs.Kotlin.Coroutines.coreMt)
                implementation(Libs.JsonParser.kotlinSerializationCore)
                implementation(Libs.NetworkingKtor.ktorCore)
                implementation(Libs.NetworkingKtor.ktorMockEngine)
                implementation(Libs.NetworkingKtor.ktorSerialization)
                implementation(Libs.SqlDelight.runtime)
                implementation(Libs.Kotlin.dateTime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin(Libs.Test.kotlinTestCommon))
                implementation(kotlin(Libs.Test.kotlinTestAnnotationsCommon))
                implementation(Libs.NetworkingKtor.ktorMockEngine)
                implementation(Libs.NetworkingKtor.ktorSerialization)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(Libs.AndroidX.coreKtx)
                implementation(Libs.NetworkingKtor.ktorAndroid)
                implementation(Libs.SqlDelight.driverAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(Libs.NetworkingKtor.ktorMockEngine)
                implementation(Libs.NetworkingKtor.ktorAndroid)
                implementation(kotlin(Libs.Test.kotlinTest))
                implementation(kotlin(Libs.Test.kotlinTestJunit))
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(Libs.NetworkingKtor.ktoriOS)
                implementation(Libs.SqlDelight.driveriOS)
            }
        }
        val iosTest by getting
    }
}
android {
    compileSdkVersion(config.AppAndroidConfig.Sdk.compileVersion)
        sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
        sourceSets["main"].java.srcDir("src/androidMain/kotlin")
        defaultConfig {
        minSdkVersion(config.AppAndroidConfig.Sdk.minimalVersion)
        targetSdkVersion(config.AppAndroidConfig.Sdk.targetVersion)
        versionCode = config.AppAndroidConfig.Application.versionCode_libModules
        versionName = config.AppAndroidConfig.Application.versionName_displayed
    }
}
val packForXcode by tasks.creating(Sync::class) {
    group = "build"
    var mode = "DEBUG"
    if (project.hasProperty("CONFIGURATION")) {
        mode = project.property("CONFIGURATION") as String
    }
    var sdkName = "iphonesimulator"
    if (project.hasProperty("SDK_NAME")) {
        sdkName = project.property("SDK_NAME") as String
    }
    val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
    val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
    inputs.property("mode", mode)
    dependsOn(framework.linkTask)
    val targetDir = File(rootDir, "../ios/Ebolapp/Resources/Frameworks/Common/$mode")
    from({ framework.outputDirectory })
    into(targetDir)
}
tasks.getByName("build").dependsOn(packForXcode)
