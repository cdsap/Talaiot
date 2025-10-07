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
    implementation(project(":library:plugins:influxdb:influxdb2-publisher"))
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    testImplementation(libs.rethinkdbDriver)
    testImplementation(libs.influxdbJava)
    testImplementation(project(":library:core:talaiot-test-utils"))
}
