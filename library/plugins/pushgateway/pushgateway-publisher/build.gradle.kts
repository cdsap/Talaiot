plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "pushgateway-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("io.prometheus:simpleclient_pushgateway:0.15.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
}
