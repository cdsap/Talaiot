plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.graph"
    artifact = "graph"
    group = "com.cdsap.talaiot.plugin"
    mainClass = "com.cdsap.talaiot.plugin.graph.TalaiotGraphPlugin"
    version = "1.3.6-SNAPSHOT"
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation(project(":library:plugins:graph:graph-publisher"))
}
