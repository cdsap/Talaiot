plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.rethinkdb"
    artifact = "rethinkdb"
    group = "com.cdsap.talaiot.plugin"
    mainClass = "com.cdsap.talaiot.plugin.rethinkdb.TalaiotRethinkdbPlugin"
    version = "1.3.6-SNAPSHOT"
}

dependencies {
    implementation(project(":library:plugins:rethinkdb:rethinkdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("com.rethinkdb:rethinkdb-driver:2.3.3")
}

