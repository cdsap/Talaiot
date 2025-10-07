package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.net.URI

/**
 * TalaiotKotlinLib Plugin represents a build configuration
 * of java-libraries]/kotlin modules.
 * These modules will require common build logic for unit tests and jacoco
 */
class TalaiotKotlinLibPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target
            .extensions
            .create<BaseConfiguration>("talaiotLib")

        target.plugins.apply("kotlin")
        target.plugins.apply("maven-publish")
        target.plugins.apply("signing")
        target.plugins.apply("java-library")
        target.plugins.apply("org.jlleitschuh.gradle.ktlint")
        target.plugins.apply("com.vanniktech.maven.publish")

        target.repositories {
            mavenCentral()
            gradlePluginPortal()
        }

        val libs = target.extensions.getByType<VersionCatalogsExtension>().named("libs")

        target.setUpJunitPlatform()
        target.setUpKtlint()

        target.extensions.getByType(KotlinJvmProjectExtension::class.java).apply {
            jvmToolchain(17)
        }
        target.setUpPublishing(Type.LIBRARY)

        target.afterEvaluate {
            val extension = extensions.getByType<BaseConfiguration>()
            setProjectVersion(extension.version)
            setProjectGroup(extension.group, Type.LIBRARY)
            collectUnitTestLibs()
            setUpSigning("TalaiotLib")
        }

        target.dependencies {
            add("testImplementation", libs.findLibrary("mockitoKotlin").get())
            add("testImplementation", libs.findLibrary("kotlintestRunner").get())
        }
    }
}
