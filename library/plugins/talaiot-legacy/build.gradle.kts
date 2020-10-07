plugins {
    `kotlin-dsl`
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot"
    artifact = "talaiot"
    group = "com.cdsap"
    mainClass = "com.cdsap.talaiot.plugin.TalaiotPlugin"
    version = "1.3.6-SNAPSHOT"
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation(project(":library:plugins:base:base-publisher"))
    implementation(project(":library:plugins:hybrid:hybrid-publisher"))
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    implementation("com.github.oshi:oshi-core:3.13.3")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation(gradleTestKit())
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:talaiot-test-utils"))
}
