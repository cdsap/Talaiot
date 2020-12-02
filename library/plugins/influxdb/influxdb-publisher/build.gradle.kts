plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "influxdb-publisher"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
