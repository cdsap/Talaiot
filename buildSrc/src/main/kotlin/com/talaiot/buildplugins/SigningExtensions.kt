package com.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.extra
import org.gradle.plugins.signing.SigningExtension

fun Project.setUpSigning(vararg publications: String) {
    System.getenv("SIGNING_PASSWORD")?.let {
        extra.set("signing.keyId", System.getenv("SIGNING_KEY"))
        extra.set("signing.password", System.getenv("SIGNING_PASSWORD"))
        extra.set("signing.secretKeyRingFile", "${rootProject.rootDir}/${System.getenv("SIGNING_FILE")}")
        configure<SigningExtension> {
            publications.forEach {
                sign(publication(it))
            }

        }
    }
}
