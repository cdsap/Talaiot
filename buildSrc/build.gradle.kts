plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
    implementation("com.gradle.publish:plugin-publish-plugin:0.12.0")
    testImplementation("junit:junit:4.12")
}

gradlePlugin {
    plugins {
        register("PluginTalaiot") {
            id = "talaiotPlugin"
            implementationClass = "com.talaiot.buildplugins.TalaiotPlugin"
        }
        register("PluginPublisher") {
            id = "publisherPlugin"
            implementationClass = "com.talaiot.buildplugins.PublisherPlugin"
        }
        register("CorePublisher") {
            id = "corePlugin"
            implementationClass = "com.talaiot.buildplugins.CorePlugin"
        }
    }
}