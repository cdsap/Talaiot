pluginManagement {
    repositories {
        mavenLocal()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "com.cdsap.talaiot") {
                useModule("com.cdsap:talaiot:${requested.version}")
            }
        }
    }
}