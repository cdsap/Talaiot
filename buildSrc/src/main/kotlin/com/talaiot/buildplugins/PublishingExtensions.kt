package com.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.publish.Publication
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.*
import java.net.URI

fun Project.setUpPublishing(
    type: Type
) {

    val extension = if (type == Type.LIBRARY) {
        extensions.getByType<BaseConfiguration>()
    } else {
        extensions.getByType<TalaiotPluginConfiguration>()
    }
    val artifact = getArtifact(extension.artifact, this)

    configure<JavaPluginExtension> {
        withJavadocJar()
        withSourcesJar()
    }
    configure<PublishingExtension> {

        repositories {
            maven {
                name = "Snapshots"
                url = URI("https://oss.jfrog.org/artifactory/oss-snapshot-local")

                credentials {
                    username = System.getenv("USERNAME_SNAPSHOT")
                    password = System.getenv("PASSWORD_SNAPSHOT")
                }
            }
        }

        publications {

            register("TalaiotLib", MavenPublication::class) {
                from(components.findByName("java"))
                artifactId = artifact
                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }
                pom {
                    name.set("Talaiot")
                    url.set("https://github.com/cdsap/Talaiot/")
                    description.set(
                        "is a simple and extensible plugin to track timing in your Gradle Project."
                    )
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("http://opensource.org/licenses/MIT")
                            distribution.set("repo")
                        }
                    }
                    developers {
                        developer {
                            id.set("Malinskiy")
                            name.set("Anton Malinskiy")
                        }
                        developer {
                            id.set("mokkun")
                            name.set("Mozart Petter")
                        }
                        developer {
                            id.set("cdsap")
                            name.set("Inaki Villar")
                        }
                        developer {
                            id.set("MyDogTom")
                            name.set("Svyatoslav Chatchenko")
                        }
                    }
                }
            }
        }
    }
}

fun Project.publication(value: String) : Publication =
    (extensions.getByName("publishing")
            as PublishingExtension).publications[value]

fun Project.setProjectGroup(
    configurationGroup: String?,
    type: Type
) {
    group = configurationGroup ?: when (type) {
        Type.LIBRARY -> Constants.DEFAULT_GROUP_LIBRARY
        Type.PLUGIN -> Constants.DEFAULT_GROUP_PLUGIN
    }
}

fun Project.setProjectVersion(configurationVersion: String?) {
    version = configurationVersion ?: Constants.TALAIOT_VERSION
}

fun getArtifact(configurationArtifactId: String?, project: Project) =
    configurationArtifactId ?: project.name


enum class Type {
    PLUGIN,
    LIBRARY
}
