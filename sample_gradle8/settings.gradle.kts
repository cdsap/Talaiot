pluginManagement {
    repositories {
        maven(url = uri("https://central.sonatype.com/repository/maven-snapshots/"))
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention").version("1.0.0")
}
