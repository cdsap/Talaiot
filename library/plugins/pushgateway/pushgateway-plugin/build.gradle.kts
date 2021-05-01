plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "io.github.cdsap.talaiot.plugin.pushgateway"
    artifact = "pushgateway"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "io.github.cdsap.talaiot.plugin.pushgateway.TalaiotPushgatewayPlugin"
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Pushgateway Plugin"
}

dependencies {
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
}
