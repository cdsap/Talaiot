plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "hybrid-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:elastic-search:elastic-search-publisher"))
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
