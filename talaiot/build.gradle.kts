import org.gradle.kotlin.dsl.`kotlin-dsl`

val kotlinVersion = "1.2.61"
val playPublisher = System.getenv("PUBLISHER_VERSION") ?: "2.0.0-beta2"

buildscript {
    val kotlinVersion = "1.2.61"

    repositories {
        jcenter()
        mavenCentral()
        google()
        maven { url = uri("https://maven.fabric.io/public") }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }
}


plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
}

group = "cdsap"
version = "0.1"


repositories {
    mavenLocal()
    google()
    jcenter()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre7:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
}

configurations.all {
    resolutionStrategy {
        force("org.codehaus.groovy:groovy-all:2.4.15")
    }
}

gradlePlugin {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }

    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TimeTrackerPlugin"
        }
    }

}

publishing {
    repositories {
        maven(url = "build/repository")
        mavenLocal()
    }
}
