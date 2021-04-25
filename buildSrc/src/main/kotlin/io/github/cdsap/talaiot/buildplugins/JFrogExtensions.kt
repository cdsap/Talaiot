package io.github.cdsap.talaiot.buildplugins



import com.jfrog.bintray.gradle.BintrayExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType

fun Project.setUpJfrog() {
    val extension = extensions.getByType<BaseConfiguration>()
    configure<BintrayExtension> {
        user = System.getenv("USERNAME_BINTRAY")
        key =  System.getenv("KEY_BINTRAY")
        setPublications("TalaiotLib")

        pkg.apply {
            publish = true
            repo = "Talaiot"
            name = extension.artifact
            userOrg = "cdsap"
            setLicenses("MIT")
            vcsUrl = "https://github.com/cdsap/Talaiot"
        }
    }
}