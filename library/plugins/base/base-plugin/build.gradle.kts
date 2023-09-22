
import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin
import org.gradle.kotlin.dsl.pluginBundle

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "base-plugin"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:base:base-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}

gradlePlugin {
    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.base"
            displayName = "Talaiot, Base Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.base.TalaiotBasePlugin"
            description =
                "Talaiot, Base Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
        }
    }
}

pluginBundle {
    website = "https://github.com/cdsap/Talaiot"
    vcsUrl = "https://github.com/cdsap/Talaiot"
    tags = listOf("tracking", "kotlin")
}
