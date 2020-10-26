plugins {
    id("kotlinLib")
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
