
buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.2.1")
        classpath("com.cdsap:talaiot-gradle-4:0.1.8.5-SNAPSHOT")
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