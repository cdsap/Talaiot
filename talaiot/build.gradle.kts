import org.gradle.kotlin.dsl.`kotlin-dsl`

val kotlinVersion = "1.3.0"

buildscript {
    val kotlinVersion = "1.3.0"

    repositories {
        jcenter()
        mavenCentral()
        google()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
        maven { url = uri("https://maven.fabric.io/public") }
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        classpath("io.ktor:ktor-client:1.0.0")
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
    maven(url = "https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    compile("io.ktor:ktor-client:1.0.0")
    compile("com.squareup.okhttp3:okhttp:3.12.0")
}

gradlePlugin {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
        compile("io.ktor:ktor-client:1.0.0")
        compile ("io.ktor:ktor-client-okhttp:1.0.0")
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
