
buildscript {
    repositories {
        google()
        jcenter()
        mavenLocal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.2.1")
        classpath("com.cdsap:talaiot:0.1.8.4-SNAPSHOT")
        classpath(kotlin("gradle-plugin", version = "1.3.10"))
    }
}

repositories {
    google()
    jcenter()
    mavenLocal()
    maven(url = "https://dl.bintray.com/kotlin/ktor")
    gradlePluginPortal()

}