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
include(":library:plugins:base:base-plugin")
include(":library:plugins:base:base-publisher")
include(":library:plugins:hybrid:hybrid-publisher")
include(":library:plugins:graph:graph-publisher")
