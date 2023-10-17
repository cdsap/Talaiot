import io.github.cdsap.talaiot.buildplugins.Constants
import org.gradle.kotlin.dsl.gradlePlugin

plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    artifact = "rethinkdb"
}

version = Constants.TALAIOT_VERSION
group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN

dependencies {
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
}

gradlePlugin {
    website.set("https://github.com/cdsap/Talaiot")
    vcsUrl.set("https://github.com/cdsap/Talaiot")

    plugins {
        register(project.name) {
            id = "io.github.cdsap.talaiot.plugin.rethinkdb"
            displayName = "Talaiot, RethinkDb Plugin"
            implementationClass = "io.github.cdsap.talaiot.plugin.rethinkdb.TalaiotRethinkdbPlugin"
            description =
                "Talaiot, RethinkDb Plugin, simple and extensible plugin to track task and build times in your Gradle Project."
            tags.addAll("tracking", "kotlin")
        }
    }
}
