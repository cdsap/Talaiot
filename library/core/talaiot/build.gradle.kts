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
    implementation("io.github.cdsap:commandline-value-source:0.1.0")
    implementation("io.github.cdsap:jdk-tools-parser:0.1.1")
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
