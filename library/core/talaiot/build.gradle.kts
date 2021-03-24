plugins {
    id("kotlinLib")
    `java-gradle-plugin`
    `kotlin-dsl`
}

talaiotLib {
    artifact = "talaiot"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    api(project(":library:core:talaiot-logger"))
    api(project(":library:core:talaiot-request"))
    implementation("com.github.oshi:oshi-core:5.5.0")
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.5.2")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
}

