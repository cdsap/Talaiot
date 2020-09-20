plugins {
    `kotlin`
}

repositories {
    mavenCentral()
}


dependencies {
    implementation(project(":library:talaiot-logger"))
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("org.testcontainers:testcontainers:1.11.3")
    api("org.testcontainers:influxdb:1.11.3")
    api("org.testcontainers:elasticsearch:1.12.0")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}