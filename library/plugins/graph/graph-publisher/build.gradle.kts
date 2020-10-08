plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:talaiot"))
    testImplementation(project(":library:talaiot-test-utils"))
}
