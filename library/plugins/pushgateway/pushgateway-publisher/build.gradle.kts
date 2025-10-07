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
    implementation(libs.prometheusPushgateway)
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation(libs.kohttp)
}
