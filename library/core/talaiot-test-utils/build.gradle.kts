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
    implementation(libs.testcontainers)
    api(libs.testcontainersInfluxdb)
    api(libs.testcontainersElasticsearch)
    implementation(libs.jna)
}
