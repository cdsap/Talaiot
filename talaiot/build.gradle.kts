import com.novoda.gradle.release.PublishExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompile
import org.gradle.api.publish.maven.MavenPom

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    groovy
    kotlin("jvm") version "1.3.11"
    id("com.gradle.plugin-publish") version "0.10.0"
    id("com.novoda.bintray-release")
}


val versionTalaiot = "0.1.15"

group = "com.cdsap"
version = versionTalaiot

gradlePlugin {
    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }
        dependencies {
            api("io.github.rybalkinsd:kohttp:0.7.1")
            testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.0")
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
                username = ""
                password = ""
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

configure<PublishExtension> {
    userOrg = ""
    groupId = "com.cdsap"
    artifactId = "talaiot"
    publishVersion = versionTalaiot
    desc = "Simple and extensible plugin to track task times in your Gradle Project."
    website = "https://github.com/cdsap/Talaiot"
}

repositories {
    jcenter()
    google()
    mavenLocal()
}
