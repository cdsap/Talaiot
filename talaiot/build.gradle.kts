plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("jacoco")
    kotlin("jvm") version "1.3.60"
    id("com.gradle.plugin-publish") version "0.12.0"
}

jacoco {
    toolVersion = "0.8.3"
}

val versionTalaiot = "1.3.2-SNAPSHOT"

group = "com.cdsap"
version = versionTalaiot

gradlePlugin {
    plugins {
        register("Talaiot") {
            id = "com.cdsap.talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }
        dependencies {
            implementation("io.github.rybalkinsd:kohttp:0.10.0")
            implementation("guru.nidi:graphviz-java:0.8.3")
            implementation("org.influxdb:influxdb-java:2.15")
            implementation("com.github.oshi:oshi-core:3.13.3")
            implementation("com.google.code.gson:gson:2.8.5")
            implementation("org.elasticsearch.client:elasticsearch-rest-high-level-client:7.3.0")
            implementation("com.rethinkdb:rethinkdb-driver:2.3.3")
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
    (plugins) {
        "Talaiot" {
            displayName = "Talaiot"
            description = "Simple and extensible plugin to track task and build times in your Gradle Project."
            tags = listOf("tracking", "kotlin", "gradle")
            version = versionTalaiot
        }
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
    }
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
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
