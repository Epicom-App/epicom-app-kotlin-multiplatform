
buildscript {
    repositories {
        google()
        jcenter()
        maven(url = "http://s2.appsfactory.de/APPSfactory/Maven")
        maven(url = uri("https://plugins.gradle.org/m2/"))
        maven(url = "https://kotlin.bintray.com/kotlinx/")
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Libs.Kotlin.kotlinVersion}")
        classpath("com.android.tools.build:gradle:4.1.2")
        classpath(GradleBuildPlugins.kotlinSerialization)
        classpath("com.squareup.sqldelight:gradle-plugin:1.4.4")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven(url = "https://dl.bintray.com/kotlin/kotlinx")
        maven(url = "https://jitpack.io")
        maven(url = "http://s2.appsfactory.de/APPSfactory/Maven")
        maven(url = "http://oss.jfrog.org/artifactory/oss-snapshot-local")
    }
}

plugins {
    kotlin("jvm") version Libs.Kotlin.kotlinVersion
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}