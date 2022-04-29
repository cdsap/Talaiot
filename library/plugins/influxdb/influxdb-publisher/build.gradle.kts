plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "influxdb-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:influxdb:influxdb-common"))
    implementation("org.influxdb:influxdb-java:2.21")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
