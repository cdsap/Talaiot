package io.github.cdsap.talaiot.buildplugins

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

fun Project.setUpKtlint() = this.run {
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        debug.set(true)
        reporters {
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
            reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
        }
    }
}

