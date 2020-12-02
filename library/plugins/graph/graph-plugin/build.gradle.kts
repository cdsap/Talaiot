plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.graph"
    artifact = "graph"
    group =  com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.graph.TalaiotGraphPlugin"
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Graph Plugin"
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:graph:graph-publisher"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}
