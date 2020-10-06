plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:talaiot-test-utils"))
}
