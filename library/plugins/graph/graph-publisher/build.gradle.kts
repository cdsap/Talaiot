plugins {
    id("kotlinLib")
    `kotlin-dsl`
}

talaiotLib {
    artifact = "graph-publisher"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
