package com.talaiot.buildplugins.extensions

fun Project.setUpPublishing() = this.run {
    configure<PublishingExtension> {
        repositories {
            maven {
                name = "Snapshots"
                url = URI("http://oss.jfrog.org/artifactory/oss-snapshot-local")

                credentials {
                    username = System.getenv("USERNAME_SNAPSHOT")
                    password = System.getenv("PASSWORD_SNAPSHOT")
                }
            }
        }

        publications {
            create<MavenPublication>("maven") {
                val extension = target.extensions.getByType<TalaiotPluginExtension>()
                groupId = target.group.toString()
                artifactId = extension.artifact
                version = target.version.toString()
                from(target.components["kotlin"])
            }
        }
    }
}