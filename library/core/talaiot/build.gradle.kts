plugins {
    id("kotlinLib")
    `java-gradle-plugin`
}

talaiotLib {
    artifact = "talaiot"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    api(project(":library:core:talaiot-logger"))
    api(project(":library:core:talaiot-request"))
    implementation("com.gradle:gradle-enterprise-gradle-plugin:3.5.2")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
