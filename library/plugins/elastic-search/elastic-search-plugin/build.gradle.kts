import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin
import org.gradle.kotlin.dsl.pluginBundle

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "elasticsearch"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:elastic-search:elastic-search-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
    testImplementation("com.google.code.gson:gson:2.8.5")
}

gradlePlugin {
    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.elasticsearch"
            displayName = "Talaiot, Elastic Search Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.elasticsearch.TalaiotElasticSearchPlugin"
            description =
                "Talaiot, Elastic Search Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
        }
    }
}

pluginBundle {
    website = "https://github.com/cdsap/Talaiot"
    vcsUrl = "https://github.com/cdsap/Talaiot"
    tags = listOf("tracking", "kotlin")
}
