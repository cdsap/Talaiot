plugins {
    id("kotlinLib")
}
talaiotLib {
    artifact = "talaiot-request"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    api(project(":library:core:talaiot-logger"))
    implementation("io.github.rybalkinsd:kohttp:0.10.0")
}
