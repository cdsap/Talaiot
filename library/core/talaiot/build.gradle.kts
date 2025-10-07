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
    implementation(libs.commandlineValueSource)
    implementation(libs.jdkToolsParser)
    testImplementation(libs.kohttp)
    testImplementation(project(":library:core:talaiot-test-utils"))
}
