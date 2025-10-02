import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    `kotlin-dsl`
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "talaiot"
}

version = Constants.TALAIOT_VERSION
group = "io.github.cdsap"

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:base:base-publisher"))
    implementation(project(":library:plugins:elastic-search:elastic-search-publisher"))
    implementation(project(":library:plugins:hybrid:hybrid-publisher"))
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:plugins:influxdb:influxdb2-publisher"))
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    implementation("com.google.code.gson:gson:2.8.9")
    implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
    testImplementation(gradleTestKit())
    testImplementation("io.github.cdsap:jdk-tools-parser:0.1.1")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation("org.influxdb:influxdb-java:2.19")
    testImplementation(project(":library:core:talaiot-test-utils"))
}

gradlePlugin {
    website.set("https://github.com/cdsap/Talaiot")
    vcsUrl.set("https://github.com/cdsap/Talaiot")
    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot"
            displayName = "Talaiot"
            implementationClass = "io.github.cdsap.talaiot.plugin.TalaiotPlugin"
            description =
                "Talaiot, simple and extensible plugin to track task and build times in your Gradle Project."
            tags.addAll("tracking", "kotlin")
        }
    }
}
