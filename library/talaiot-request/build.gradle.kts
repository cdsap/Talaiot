plugins {
    id("kotlinLib")
}

dependencies {
    api(project(":library:talaiot-logger"))
    implementation("io.github.rybalkinsd:kohttp:0.10.0")
}
