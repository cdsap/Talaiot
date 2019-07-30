pluginManagement {
    repositories {
        maven { url = uri("$rootDir/../build/repository") }
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "talaiot") {
                useModule("com.cdsap:talaiot:${requested.version}")
            }
        }
    }
}