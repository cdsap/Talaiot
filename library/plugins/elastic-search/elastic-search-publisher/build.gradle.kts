plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.google.code.gson:gson:2.8.5")
    testImplementation(project(":library:talaiot-test-utils"))
}
