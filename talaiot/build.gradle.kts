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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")
    }
}


plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
    `maven-publish`
    id("com.novoda.bintray-release")
}


val artifactID = "talaiot"
kotlin {
    experimental {
        coroutines = Coroutines.ENABLE

    }
}
repositories {
    mavenLocal()
    google()
    jcenter()
    gradlePluginPortal()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
}

dependencies {
    implementation("com.novoda:bintray-release:0.9")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")

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
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")
        implementation("io.ktor:ktor-client:0.9.4")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.61")
        implementation("io.ktor:ktor-client-okhttp:0.9.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.30.2")
        testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.11")
        //  testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")

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
    publishVersion = "0.1.7"
    desc = ""
    website = "https://github.com/cdsap/Talaiot"
    issueTracker = "https://github.com/cdsap/Talaiot/issues"
    repository = "https://github.com/cdsap/Talaiot/"
}