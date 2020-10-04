plugins {
    id("kotlinLib")
    `java-gradle-plugin`
    `kotlin-dsl`
}

dependencies {
    api(project(":library:talaiot-logger"))
    api(project(":library:talaiot-request"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    implementation("com.github.oshi:oshi-core:3.13.3")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation(project(":library:talaiot-test-utils"))
}

