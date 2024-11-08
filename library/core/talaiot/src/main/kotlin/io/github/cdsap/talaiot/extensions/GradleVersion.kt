package io.github.cdsap.talaiot.extensions

import org.gradle.util.GradleVersion

fun GradleVersion.isCompatibleWithIsolatedProjects(): Boolean = GradleVersion.version("8.5") <= this.baseVersion
