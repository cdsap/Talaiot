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
    githubToken = ""
    dryDeploy = "true"
    // srcDir  = "../talaiot/src/main/kotlin"


}