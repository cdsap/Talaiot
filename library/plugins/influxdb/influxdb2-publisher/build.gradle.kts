plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "influxdb2-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("com.influxdb:influxdb-client-java:3.1.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
