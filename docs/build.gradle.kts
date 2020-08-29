plugins {
    kotlin("jvm")
    id("com.eden.orchidPlugin") version "0.17.1"

}

repositories {
    jcenter()
    maven(url = "https://kotlin.bintray.com/kotlinx")
    maven(url = "https://jitpack.io")
    maven(url = "https://dl.bintray.com/javaeden/Eden")
}


dependencies {
    orchidRuntime("io.github.javaeden.orchid:OrchidAll:0.17.1")
    orchidRuntime("io.github.javaeden.orchid:OrchidGithub:0.17.1")
}

orchid {
    theme = "Editorial"
    baseUrl = "https://cdsap.github.io/Talaiot"
    version = "1.0.0"
    githubToken = ""


}