plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "elastic-search-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(libs.elasticsearchClient)
    implementation(libs.gson)
    testImplementation(project(":library:core:talaiot-test-utils"))
}
