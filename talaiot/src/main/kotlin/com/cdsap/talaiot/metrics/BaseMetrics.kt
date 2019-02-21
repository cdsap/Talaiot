package com.cdsap.talaiot.metrics

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.util.*

class BaseMetrics(private val project: Project) : Metrics {

    private val buildId = UUID.randomUUID().toString()

    override fun get() = mapOf(
        "user" to System.getProperty("user.name"),
        "project" to project.gradle.rootProject.name,
        "buildId" to buildId,
        "os" to "${OperatingSystem.current().name}-${OperatingSystem.current().version}"
    )

}
