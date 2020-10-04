plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
   implementation(project(":library:plugins:influxdb:publisher"))
    testImplementation(project(":library:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:plugins:influxdb:publisher"))
}
