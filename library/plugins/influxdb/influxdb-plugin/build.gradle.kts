plugins {
    id("talaiotPlugin")
}

talaiotPlugin {
    idPlugin = "com.cdsap.talaiot.plugin.influxdb"
    artifact = "influxdb"
    group =  com.talaiot.buildplugins.Constants.DEFAULT_GROUP_PLUGIN
    mainClass = "com.cdsap.talaiot.plugin.influxdb.TalaiotInfluxdbPlugin"
    version =  com.talaiot.buildplugins.Constants.TALAIOT_VERSION
    displayName = "Talaiot, InfluxDb Plugin"
}

dependencies {
    implementation(project(":library:plugins:influxdb:influxdb-publisher"))
    implementation(project(":library:core:talaiot"))
    testImplementation(project(":library:core:talaiot-test-utils"))
    testImplementation("org.influxdb:influxdb-java:2.19")
}

