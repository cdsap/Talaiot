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
}

group = "com.cdsap"
version = "0.1.3"

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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")
}

gradlePlugin {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        maven(url = "https://dl.bintray.com/kotlin/ktor")
    }

    dependencies {
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")
        compile("io.ktor:ktor-client:0.9.4")
        compile("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.61")
        compile("io.ktor:ktor-client-okhttp:0.9.4")
        compile("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.30.2")
    }

    plugins {
        register("Talaiot") {
            id = "talaiot"
            implementationClass = "com.cdsap.talaiot.TalaiotPlugin"
        }
    }

}
//
publishing {
    repositories {
        mavenLocal()

    }
}
