plugins {
    id("kotlinLib")
}

dependencies {
    api(project(":library:core:talaiot-logger"))
    implementation("io.github.rybalkinsd:kohttp:0.10.0")
}