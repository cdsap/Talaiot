plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
