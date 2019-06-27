pluginManagement {
    repositories {
        maven { url = uri("$rootDir/../build/repository") }
        mavenCentral()
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "talaiot") {
                useModule("com.cdsap:talaiot:${requested.version}")
            }
        }
    }
}