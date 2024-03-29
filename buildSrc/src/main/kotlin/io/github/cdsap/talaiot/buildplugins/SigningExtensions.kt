package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.api.publish.PublishingExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.plugins.signing.SigningExtension
import java.io.File

fun Project.setUpSigning(vararg publications: String) {
    System.getenv("SIGNING_PASSWORD")?.let {
        val secring = "${rootProject.rootDir}/.signing/secring.gpg"
        extra.set("signing.keyId", System.getenv("SIGNING_KEY"))
        extra.set("signing.password", System.getenv("SIGNING_PASSWORD"))
        extra.set("signing.secretKeyRingFile", secring)
        if (File(secring).exists()) {
            configure<SigningExtension> {
                (extensions.getByName("publishing") as PublishingExtension).publications.forEach {
                    if (tasks.matching { it.name == "signPluginMavenPublication" }.size == 0) {
                        sign(it)
                    }
                }
            }
        }
    }
}
