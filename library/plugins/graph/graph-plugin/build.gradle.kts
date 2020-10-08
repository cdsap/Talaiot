plugins {
    id("talaiotPlugin")
}

dependencies {
    implementation(project(":library:talaiot"))
    implementation(project(":library:plugins:graph:graph-publisher"))
}
