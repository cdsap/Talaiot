plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "io.github.cdsap.talaiot.plugin.graph"
    artifact = "graph"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "io.github.cdsap.talaiot.plugin.graph.TalaiotGraphPlugin"
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, Graph Plugin"
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:graph:graph-publisher"))
    testImplementation(project(":library:core:talaiot-test-utils"))
}
