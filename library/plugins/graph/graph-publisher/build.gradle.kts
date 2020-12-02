plugins {
    id("kotlinLib")
    `kotlin-dsl`
}

talaiotLib {
    artifact = "graph-publisher"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation(project(":library:core:talaiot"))
    implementation("guru.nidi:graphviz-java:0.8.3")
    testImplementation(project(":library:core:talaiot-test-utils"))
}
