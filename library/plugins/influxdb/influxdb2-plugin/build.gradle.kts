import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin
import org.gradle.kotlin.dsl.pluginBundle

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "influxdb2"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:influxdb:influxdb2-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.19")
}

gradlePlugin {
    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.influxdb2"
            displayName = "Talaiot, InfluxDb 2 (Flux) Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.influxdb2.TalaiotInfluxdb2Plugin"
            description =
                "Talaiot, InfluxDb 2 (Flux) Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
        }
    }
}

pluginBundle {
    website = "https://github.com/cdsap/Talaiot"
    vcsUrl = "https://github.com/cdsap/Talaiot"
    tags = listOf("tracking", "kotlin")
}
