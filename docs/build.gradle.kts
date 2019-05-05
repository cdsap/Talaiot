plugins {
    kotlin("jvm") version "1.3.11"
    id("com.eden.orchidPlugin") version "0.16.9"

}

repositories {
    jcenter()
    maven(url = "https://jitpack.io")

}

dependencies {
    orchidRuntime("io.github.javaeden.orchid:OrchidAll:0.16.9")
}

orchid {
    theme = "Editorial"
    baseUrl = ""
    version = "1.0.0"
    githubToken = "71d7feac78b0f460fb514f6c92d0b928dc937391"
    // srcDir  = "../talaiot/src/main/kotlin"


}