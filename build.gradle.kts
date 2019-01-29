
buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath(kotlin("gradle-plugin", version = "1.3.11"))
    }
}

repositories {
    google()
    jcenter()
    mavenLocal()
    mavenCentral()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    gradlePluginPortal()

}