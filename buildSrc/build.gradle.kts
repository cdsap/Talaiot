plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.2.20")
    implementation("com.vanniktech.maven.publish.base:com.vanniktech.maven.publish.base.gradle.plugin:0.34.0")
    implementation("com.gradle.publish:plugin-publish-plugin:2.0.0")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:11.6.1")
    testImplementation("junit:junit:4.13.2")
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
