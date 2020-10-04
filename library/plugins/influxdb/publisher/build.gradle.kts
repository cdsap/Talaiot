plugins {
    id("kotlinLib")
}

dependencies {
    implementation("org.influxdb:influxdb-java:2.19")
    implementation(project(":library:talaiot"))
}
