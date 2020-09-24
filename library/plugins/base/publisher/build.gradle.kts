plugins {
    id("kotlinLib")
}

dependencies {
    implementation("com.google.code.gson:gson:2.8.5")
    implementation(project(":library:talaiot"))

}
