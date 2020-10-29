plugins {
    id("kotlinLib")
    `kotlin-dsl`
}

dependencies {
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}
