plugins {
    id("talaiotPlugin")
}


talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.rethinkdb"
    artifact = "rethinkdb"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.rethinkdb.TalaiotRethinkdbPlugin"
    version =  com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, RethinkDb Plugin"
}

dependencies {
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
}

