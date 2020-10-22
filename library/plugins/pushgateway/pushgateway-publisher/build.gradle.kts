plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation(project(":library:talaiot-request"))
    testImplementation(project(":library:talaiot-test-utils"))
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
}
