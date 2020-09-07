plugins {
    `kotlin-dsl`
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot"
    artifact = "talaiot"
    group = "com.cdsap"
    mainClass = "com.cdsap.talaiot.TalaiotPlugin"
    version = "1.3.70-SNAPSHOT"
}

dependencies {
    api(project(":library:talaiot-logger"))
    api(project(":library:talaiot-request"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    implementation("org.influxdb:influxdb-java:2.19")
    implementation("com.github.oshi:oshi-core:3.13.3")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation(gradleTestKit())
    testImplementation("org.testcontainers:testcontainers:1.11.3")
    testImplementation("org.testcontainers:influxdb:1.11.3")
    testImplementation("org.testcontainers:elasticsearch:1.12.0")
}

