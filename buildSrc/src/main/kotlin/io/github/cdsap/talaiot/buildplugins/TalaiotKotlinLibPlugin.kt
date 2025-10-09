package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.kotlin.dsl.*
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import java.net.URI
import kotlin.jvm.java

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

        target.setUpJunitPlatform()
        target.setUpKtlint()

        target.extensions.getByType(KotlinJvmProjectExtension::class.java).apply {
            jvmToolchain(11)
        }
        target.tasks.withType<Test>().configureEach {
            javaLauncher = target.extensions.getByType(JavaToolchainService::class.java).launcherFor {
                languageVersion = JavaLanguageVersion.of(17)
            }
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
            add("testImplementation", "com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
            add("testImplementation", "io.kotlintest:kotlintest-runner-junit5:3.3.2")
        }
    }
}
