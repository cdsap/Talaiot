import com.novoda.gradle.release.PublishExtension

plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    groovy
    id("jacoco")
    kotlin("jvm") version "1.3.11"
    id("com.gradle.plugin-publish") version "0.10.0"
    id("com.novoda.bintray-release")
}
jacoco {
    toolVersion = "0.8.3"
}

val versionTalaiot = "1.0.8-SNAPSHOT"



group = "com.cdsap"
version = versionTalaiot

gradlePlugin {
    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }
        dependencies {
            api("io.github.rybalkinsd:kohttp:0.10.0")
            api("guru.nidi:graphviz-java:0.8.3")
            api("org.influxdb:influxdb-java:2.15")
            api("com.github.oshi:oshi-core:3.13.3")
            api("com.google.code.gson:gson:2.8.5")
            api("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
            testImplementation("io.kotlintest:kotlintest-runner-junit5:3.3.2")
            testImplementation(gradleTestKit())
            testImplementation("org.testcontainers:testcontainers:1.11.3")
            testImplementation("org.testcontainers:influxdb:1.11.3")
            testImplementation("org.testcontainers:elasticsearch:1.12.0")
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
            url = uri("http://oss.jfrog.org/artifactory/oss-snapshot-local")

            credentials {
                username = System.getenv("USERNAME_SNAPSHOT")
                password = System.getenv("PASSWORD_SNAPSHOT")
            }
        }
        maven {
            name = "Local"
            setUrl("${project.rootDir}/build/repository")
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
                developer {
                    id.set("pkun.zip.rus@gmail.com")
                    name.set("Anton Malinskiy")
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
    mavenCentral()
    google()
    mavenLocal()
}



tasks.jacocoTestReport {
    reports {
        xml.isEnabled = true
        csv.isEnabled = true
        html.isEnabled = true
        html.destination = file("$buildDir/reports/coverage")
    }
}