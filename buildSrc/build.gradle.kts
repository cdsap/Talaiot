import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.the

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

val libs = the<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.findLibrary("kotlinGradlePlugin").get())
    implementation(libs.findLibrary("vanniktechMavenPublish").get())
    implementation(libs.findLibrary("gradlePluginPublish").get())
    implementation(libs.findLibrary("ktlintGradle").get())
    testImplementation(libs.findLibrary("junit4").get())
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
