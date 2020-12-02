plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.base"
    artifact = "base"
    group =  com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.base.TalaiotBasePlugin"
    version =  com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Base Plugin"
}

dependencies {
    implementation(project(":library:plugins:base:base-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}
