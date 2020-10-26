plugins {
    id("kotlinLib")
    `java-gradle-plugin`
    `kotlin-dsl`
}

dependencies {
    api(project(":library:core:talaiot-logger"))
    api(project(":library:core:talaiot-request"))
    api("guru.nidi:graphviz-java:0.8.3")
    implementation("com.github.oshi:oshi-core:3.13.3")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
}

