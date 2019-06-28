pluginManagement {
    repositories {
        maven { url = uri("$rootDir/../build/repository") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "talaiot") {
                useModule("com.cdsap:talaiot:${requested.version}")
            }
        }
    }
}