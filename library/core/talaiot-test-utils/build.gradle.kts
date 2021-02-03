plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "talaiot-test-utils"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot-logger"))
    implementation("org.testcontainers:testcontainers:1.11.3")
    api("org.testcontainers:influxdb:1.11.3")
    api("org.testcontainers:elasticsearch:1.12.0")

}
