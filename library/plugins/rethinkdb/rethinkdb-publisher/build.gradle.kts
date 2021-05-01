plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "rethinkdb-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
