import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin

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
    testImplementation(libs.elasticsearchClient)
    testImplementation(libs.gson)
}

gradlePlugin {
    website.set("https://github.com/cdsap/Talaiot")
    vcsUrl.set("https://github.com/cdsap/Talaiot")

    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.elasticsearch"
            displayName = "Talaiot, Elastic Search Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.elasticsearch.TalaiotElasticSearchPlugin"
            description =
                "Talaiot, Elastic Search Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
            tags.addAll("tracking", "kotlin")
        }
    }
}
