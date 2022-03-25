pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven(url = "https://jitpack.io")

    }
}
include(":library:core:talaiot")
include(":library:core:talaiot-request")
include(":library:core:talaiot-logger")
include(":library:core:talaiot-test-utils")
include(":library:plugins:base:base-plugin")
include(":library:plugins:base:base-publisher")
include(":library:plugins:elastic-search:elastic-search-plugin")
include(":library:plugins:elastic-search:elastic-search-publisher")
include(":library:plugins:graph:graph-plugin")
include(":library:plugins:graph:graph-publisher")
include(":library:plugins:hybrid:hybrid-publisher")
include(":library:plugins:influxdb:influxdb-plugin")
include(":library:plugins:influxdb:influxdb-publisher")
include(":library:plugins:pushgateway:pushgateway-plugin")
include(":library:plugins:pushgateway:pushgateway-publisher")
include(":library:plugins:rethinkdb:rethinkdb-plugin")
include(":library:plugins:rethinkdb:rethinkdb-publisher")
include(":library:plugins:talaiot-standard")