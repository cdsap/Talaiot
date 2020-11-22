plugins {
    id("kotlinLib")
}

talaiotLib {
    artifact = "elastic-search-publisher"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
