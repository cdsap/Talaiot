plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    maven {
        url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.10")
    implementation("com.vanniktech.maven.publish.base:com.vanniktech.maven.publish.base.gradle.plugin:0.34.0")
    implementation("com.gradle.publish:plugin-publish-plugin:1.2.1")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.6.1")
    testImplementation("junit:junit:4.12")
}

gradlePlugin {
    plugins {
        register("PluginTalaiot") {
            id = "talaiotPlugin"
            implementationClass = "io.github.cdsap.talaiot.buildplugins.TalaiotPlugin"
        }
        register("PluginPublisher") {
            id = "publisherPlugin"
            implementationClass = "com.talaiot.buildplugins.PublisherPlugin"
        }
        register("TalaiotKotlinLibPlugin") {
            id = "kotlinLib"
            implementationClass = "io.github.cdsap.talaiot.buildplugins.TalaiotKotlinLibPlugin"
        }
    }
}
