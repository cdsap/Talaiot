plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "talaiot-test-utils"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot-logger"))
    implementation("org.testcontainers:testcontainers:1.21.3")
    api("org.testcontainers:influxdb:1.21.3")
    api("org.testcontainers:elasticsearch:1.21.3")
    implementation("net.java.dev.jna:jna:5.18.1")
}
