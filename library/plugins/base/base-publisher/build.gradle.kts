plugins {
    id("kotlinLib")
}

talaiotLib{
    artifact = "base-publisher"
    group = com.talaiot.buildplugins.Constants.DEFAULT_GROUP_LIBRARY
    version = com.talaiot.buildplugins.Constants.TALAIOT_VERSION
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.5")
    implementation(project(":library:core:talaiot"))
}
