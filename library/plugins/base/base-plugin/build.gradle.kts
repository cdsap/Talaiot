plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "io.github.cdsap.talaiot.plugin.base"
    artifact = "base-plugin"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "io.github.cdsap.talaiot.plugin.base.TalaiotBasePlugin"
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Base Plugin"
}

dependencies {
    implementation(project(":library:plugins:base:base-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}
