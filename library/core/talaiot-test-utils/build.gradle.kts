plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:core:talaiot-logger"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("org.testcontainers:testcontainers:1.11.3")
    api("org.testcontainers:influxdb:1.11.3")
    api("org.testcontainers:elasticsearch:1.12.0")

}
