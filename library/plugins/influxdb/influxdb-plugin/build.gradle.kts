plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "io.github.cdsap.talaiot.plugin.influxdb"
    artifact = "influxdb"
    group = io.github.cdsap.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "io.github.cdsap.talaiot.plugin.influxdb.TalaiotInfluxdbPlugin"
    version = io.github.cdsap.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, InfluxDb Plugin"
}

dependencies {
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.19")
}

