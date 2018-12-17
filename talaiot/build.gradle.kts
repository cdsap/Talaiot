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
version = "0.1.6"

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
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.2.61")
        implementation("io.ktor:ktor-client:0.9.4")
        implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.2.61")
        implementation("io.ktor:ktor-client-okhttp:0.9.4")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:0.30.2")
        testImplementation("io.kotlintest:kotlintest-runner-junit5:3.1.11")
      testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.0.0-RC1")

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
