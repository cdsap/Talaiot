pluginManagement {
    repositories {
        mavenCentral()
        jcenter()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
        gradlePluginPortal()
        maven(url = "https://jitpack.io")

    }
}
include(":library:docs")
include(":library:talaiot")
include(":library:talaiot-request")
include(":library:talaiot-logger")
include(":library:talaiot-test-utils")
include(":library:plugins:talaiot-legacy")
include(":library:plugins:base:plugin")
include(":library:plugins:base:publisher")
