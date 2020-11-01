plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.elasticsearch"
    artifact = "elasticsearch"
    group = "com.cdsap.talaiot.plugin"
    mainClass = "com.cdsap.talaiot.plugin.elasticsearch.TalaiotElasticSearchPlugin"
    version = "1.3.6-SNAPSHOT"
}

dependencies {
    implementation(project(":library:plugins:elastic-search:elastic-search-publisher"))
    implementation(project(":library:talaiot"))
    testImplementation(project(":library:talaiot-test-utils"))
    testImplementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    testImplementation("com.google.code.gson:gson:2.8.5")
}
