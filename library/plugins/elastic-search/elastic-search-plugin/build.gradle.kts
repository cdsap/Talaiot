plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.elasticsearch"
    artifact = "elasticsearch"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.elasticsearch.TalaiotElasticSearchPlugin"
    version =  com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Elastic Search Plugin"
}

dependencies {
    implementation(project(":library:plugins:elastic-search:elastic-search-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    testImplementation("com.google.code.gson:gson:2.8.5")
}
