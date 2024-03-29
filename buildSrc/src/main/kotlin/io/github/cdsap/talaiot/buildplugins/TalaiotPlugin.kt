package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.*
import java.net.URI

/**
 * Talaiot Plugin abstracts the build logic for modules used as Gradle Plugin.
 * Applies publication Configuration using plugins maven and gradle.
 */
class TalaiotPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target
            .extensions
            .create<TalaiotPluginConfiguration>("talaiotPlugin")

        target.plugins.apply("java-gradle-plugin")
        target.plugins.apply("maven-publish")
        target.plugins.apply("signing")
        target.plugins.apply("kotlin")
        target.plugins.apply("java-library")
        target.plugins.apply("application")
        target.plugins.apply("com.gradle.plugin-publish")
        target.plugins.apply("org.jlleitschuh.gradle.ktlint")

        target.repositories {
            mavenCentral()
            maven { url = URI("https://plugins.gradle.org/m2/") }
        }

        target.setUpJunitPlatform()
        target.setUpPublishing(Type.PLUGIN)

        target.afterEvaluate {
            collectUnitTest()
            setUpSigning("TalaiotLib", "pluginMaven")
        }
        target.tasks.withType<Tar> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        target.tasks.withType<Zip> {
            duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        }
        target.dependencies {
            add("testImplementation", "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
            add("testImplementation", "io.kotlintest:kotlintest-runner-junit5:3.3.2")
        }
    }
}

