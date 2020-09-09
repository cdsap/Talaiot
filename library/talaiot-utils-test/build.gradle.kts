plugins {
    `kotlin`
  //  kotlin("jvm")
}

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.72")
    implementation("org.testcontainers:testcontainers:1.11.3")
    implementation("org.testcontainers:influxdb:1.11.3")
    implementation("org.testcontainers:elasticsearch:1.12.0")

}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}