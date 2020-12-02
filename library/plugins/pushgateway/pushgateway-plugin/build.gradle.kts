plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.pushgateway"
    artifact = "pushgateway"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.pushgateway.TalaiotPushgatewayPlugin"
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Pushgateway Plugin"
}

dependencies {
    implementation(project(":library:plugins:pushgateway:pushgateway-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("io.github.rybalkinsd:kohttp:0.10.0")
}
