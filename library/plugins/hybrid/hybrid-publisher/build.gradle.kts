plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:talaiot-test-utils"))
}
