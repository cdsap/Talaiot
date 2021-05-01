plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "io.github.cdsap.talaiot.plugin.rethinkdb"
    artifact = "rethinkdb"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "io.github.cdsap.talaiot.plugin.rethinkdb.TalaiotRethinkdbPlugin"
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, RethinkDb Plugin"
}

dependencies {
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
}
