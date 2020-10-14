plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:plugins:base:base-publisher"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:talaiot-test-utils"))
}
