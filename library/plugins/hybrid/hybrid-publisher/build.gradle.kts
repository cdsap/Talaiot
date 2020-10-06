plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:plugins:base:base-publisher"))
    testImplementation(project(":library:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.19")

}
