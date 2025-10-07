package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.file.DuplicatesStrategy
import org.gradle.api.tasks.bundling.Tar
import org.gradle.api.tasks.bundling.Zip
import org.gradle.kotlin.dsl.*

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
        target.plugins.apply("com.vanniktech.maven.publish")

        target.repositories {
            mavenCentral()
            gradlePluginPortal()
        }

        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

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
            add("testImplementation", libs.findLibrary("mockitoKotlin").get())
            add("testImplementation", libs.findLibrary("kotlintestRunner").get())
        }
    }
}

