pluginManagement {
    repositories {
        mavenCentral()
        google()
        jcenter()
        mavenLocal()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
        gradlePluginPortal()
        maven(url = "https://jitpack.io")

    }
}
include(":library:docs")
include(":library:talaiot")
include(":library:talaiot-request")
include(":library:talaiot-logger")
include(":library:talaiot-utils-test")
