plugins {
    id("kotlinLib")
    `kotlin-dsl`
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
