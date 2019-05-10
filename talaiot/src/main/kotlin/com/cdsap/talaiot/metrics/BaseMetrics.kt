package com.cdsap.talaiot.metrics

import org.gradle.api.Project
import org.gradle.internal.os.OperatingSystem
import java.util.*

/**
 * BaseMetrics provided by default for all the builds.
 * Metrics included:
 *  -- User
 *  -- Project Name
 *  -- Os
 *  -- BuildId
 *
 * The only exception is the buildId, due problems of high cardinality or requirements
 * on your build you may want to disable it.
 *
 */
class BaseMetrics(
    /**
     * Gradle Project used to access the different configurations like the project Name
     */
    private val project: Project,
    /**
     * Configuration for the generation of the buildId.
     */
    private val generateBuildId: Boolean
) : Metrics {


    /**
     * Retrieve the values for the BaseMetrics defined
     *
     * @return collection of BaseMetrics
     */
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
