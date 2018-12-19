import com.jfrog.bintray.gradle.BintrayExtension
import com.novoda.gradle.release.PublishExtension
import org.gradle.kotlin.dsl.`kotlin-dsl`
import org.jetbrains.kotlin.gradle.dsl.Coroutines


buildscript {

    repositories {
        jcenter()
        mavenCentral()
        google()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
    }
}


plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.novoda.bintray-release")
}

val artifactID = "talaiot"

repositories {
    mavenLocal()
    google()
    jcenter()
    gradlePluginPortal()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    implementation("com.novoda:bintray-release:0.9")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")

}

val test by tasks.getting(Test::class) {
    useJUnitPlatform { }
}

gradlePlugin {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
    }

    dependencies {
        implementation("com.novoda:bintray-release:0.9")
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.11")
        implementation("io.ktor:ktor-client:1.0.1")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.10")
        implementation("io.ktor:ktor-client-okhttp:1.0.1")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.0.1")
        testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.11")
    }

    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }
    }

}

configure<PublishExtension> {
    userOrg = ""
    groupId = "com.cdsap"
    artifactId = "talaiot"
    publishVersion = "0.1.8-SNAPSHOT"
    desc = ""
    website = "https://github.com/cdsap/Talaiot"
    issueTracker = "https://github.com/cdsap/Talaiot/issues"
    repository = "https://github.com/cdsap/Talaiot/"
}