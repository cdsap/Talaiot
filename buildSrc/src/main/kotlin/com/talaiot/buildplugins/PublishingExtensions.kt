package com.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.api.attributes.LibraryElements
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.file.CopySpec
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.jvm.tasks.Jar
import org.gradle.kotlin.dsl.*
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import java.net.URI

fun Project.setUpPublishing(
    namePublication: String,
    configurationArtifactId: String?
) {

    this.configure<PublishingExtension> {
        repositories {
            maven {
                name = "Snapshots"
                url = URI("http://oss.jfrog.org/artifactory/oss-snapshot-local")

                credentials {
                    username = System.getenv("USERNAME_SNAPSHOT")
                    password = System.getenv("PASSWORD_SNAPSHOT")
                }
            }
        }

        publications {
            create<MavenPublication>(namePublication) {
                groupId = group.toString()
                artifactId = getArtifact(configurationArtifactId, project)
                version = version.toString()
                from(components["java"])
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
            }
        }
    }
}

fun Project.setProjectGroup(
    configurationGroup: String?,
    default: String
) {
    group = configurationGroup ?: default
}

fun Project.setProjectGroup(
    default: String
) {
    group = default
}

fun Project.setProjectVersion(configurationVersion: String?) {
    version = configurationVersion ?: Constants.TALAIOT_VERSION
}

fun Project.setProjectVersion() {
    version = Constants.TALAIOT_VERSION
}

fun getArtifact(configurationArtifactId: String?, project: Project) =
    configurationArtifactId ?: project.name
