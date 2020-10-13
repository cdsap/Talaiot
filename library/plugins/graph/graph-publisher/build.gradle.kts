plugins {
    id("kotlinLib")
    `kotlin-dsl`
}

dependencies {
    implementation(project(":library:talaiot"))
    testImplementation(project(":library:talaiot-test-utils"))
}
