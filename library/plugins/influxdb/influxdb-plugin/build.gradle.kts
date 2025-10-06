import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "influxdb"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.25")
}

gradlePlugin {
    website.set("https://github.com/cdsap/Talaiot")
    vcsUrl.set("https://github.com/cdsap/Talaiot")
    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.influxdb"
            displayName = "Talaiot, InfluxDb Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.influxdb.TalaiotInfluxdbPlugin"
            description =
                "Talaiot, InfluxDb Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
            tags.addAll("tracking", "kotlin")
        }
    }
}
