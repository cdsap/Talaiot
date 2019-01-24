import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.gradle.api.publish.maven.MavenPom

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    groovy
    kotlin("jvm") version "1.3.11"
    id("com.gradle.plugin-publish") version "0.10.0"
}

group = "com.cdsap"
version = "0.1.8.4-SNAPSHOT"

gradlePlugin {
    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }

        dependencies {
            api("io.ktor:ktor-client-okhttp:1.1.1")
            testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.11")
            testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")
        }
    }
}

pluginBundle {
    website = "https://github.com/cdsap/Talaiot/"
    vcsUrl = "https://github.com/cdsap/Talaiot/"
    tags = listOf("kotlin", "gradle", "kotlin-dsl")

    mavenCoordinates {
        groupId = project.group as String
        artifactId = "talaiot"
    }
}
publishing {
    repositories {
        maven {
            name = "Snapshots"
            url = uri("https://nexus.default.svc.agoda.mobi/repository/maven-snapshots")

            credentials {
                username = "admin" //project.findProperty("SONATYPE_NEXUS_USERNAME")?.toString()
                password = "admin123" //project.findProperty("SONATYPE_NEXUS_PASSWORD")?.toString()

            }
        }
    }
}


val sourcesJar by tasks.registering(Jar::class) {
    classifier = "sources"
    from(sourceSets["main"].allSource)
    dependsOn(tasks["classes"])
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

afterEvaluate {

    publishing.publications.named<MavenPublication>("pluginMaven") {
        artifactId = "talaiot"
        artifact(sourcesJar.get())
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
                    id.set("inaki.seri@gmail.com")
                    name.set("Inaki Villar")
                }
            }

        }
    }
}
repositories {
    jcenter()
    google()
    mavenLocal()
}
