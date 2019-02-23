package com.cdsap.talaiot.metrics

import org.gradle.api.Project


class GradleMetrics(private val project: Project) : Metrics {

    override fun get(): Map<String, String> {
        val gradleMetrics = mutableMapOf<String, String>()
        project.gradle.startParameter.apply {
            gradleMetrics["gradleCaching"] = "${this.isBuildCacheEnabled}"
            gradleMetrics["gradleParallel"] = "${this.isParallelProjectExecutionEnabled}"
            gradleMetrics["gradleProfile"] = "${this.isProfile}"
            gradleMetrics["gradleOffline"] = "${this.isOffline}"
        }
        if (hasProperty("org.gradle.daemon")) {
            gradleMetrics["gradleDaemon"] = property("org.gradle.daemon") as String
        }
        gradleMetrics["gradleVersion"] = project.gradle.gradleVersion
        return gradleMetrics

    }

    private fun hasProperty(property: String) = project.gradle.rootProject.hasProperty(property)
    private fun property(property: String) = project.property(property)
}