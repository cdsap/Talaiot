import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "pushgateway"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
}

gradlePlugin {
    website.set("https://github.com/cdsap/Talaiot")
    vcsUrl.set("https://github.com/cdsap/Talaiot")

    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.pushgateway"
            displayName = "Talaiot, Pushgateway Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.pushgateway.TalaiotPushgatewayPlugin"
            description =
                "Talaiot, Pushgateway Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
            tags.addAll("tracking", "kotlin")
        }
    }
}
