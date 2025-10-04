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
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.google.code.gson:gson:2.13.2")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
