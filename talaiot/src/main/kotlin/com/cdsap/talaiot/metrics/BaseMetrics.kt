package com.cdsap.talaiot.metrics

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.util.*

/**
 * BaseMetrics provided by default for all the builds.
 *
 * The only exception is the buildId, due problems of high cardinality or requirements
 * on your build you may want to disable it.
 *
 */
class BaseMetrics(
    private val project: Project,
    private val generateBuildId: Boolean
) : Metrics {

    override fun get(): Map<String, String> {
        val baseMetrics = mutableMapOf(
            "user" to System.getProperty("user.name"),
            "project" to project.gradle.rootProject.name,
            "os" to "${OperatingSystem.current().name}-${OperatingSystem.current().version}"
        )
        if (generateBuildId) {
            baseMetrics["buildId"] = UUID.randomUUID().toString()
        }
        return baseMetrics
    }
}
