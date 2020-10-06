plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.influxdb"
    artifact = "influxdb"
    group = "com.cdsap.talaiot.plugin"
    mainClass = "com.cdsap.talaiot.plugin.influxdb.TalaiotInfluxdbPlugin"
    version = "1.3.6-SNAPSHOT"
}

dependencies {
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:talaiot"))
}

