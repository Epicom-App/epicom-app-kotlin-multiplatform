package config

object AppAndroidConfig {
    object Application {
        const val id = "de.ebolapp"
        const val versionCode_libModules = 1
        const val versionName_displayed = "1"
    }
    object Sdk {
        const val compileVersion = 30
        const val minimalVersion = 21
        const val targetVersion = 30
    }
    object TestRunner {
        const val default = "androidx.test.runner.AndroidJUnitRunner"
    }
}
