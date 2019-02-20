package com.cdsap.talaiot.metrics

import org.gradle.api.Project

class GradleMetrics(private val project: Project) : Metrics {

    override fun get(): Map<String, String> {
        val gradleMetrics = mutableMapOf<String, String>()

        if (hasProperty("org.gradle.caching")) {
            gradleMetrics["gradleCaching"] = property("org.gradle.caching") as String
        }
        if (hasProperty("org.gradle.daemon")) {
            gradleMetrics["gradleDaemon"] = property("org.gradle.daemon") as String
        }
        if (hasProperty("org.gradle.parallel")) {
            gradleMetrics["gradleParallel"] = property("org.gradle.parallel") as String
        }
        if (hasProperty("org.gradle.configureondemand")) {
            gradleMetrics["gradleConfigurationOnDemand"] = property("org.gradle.configureondemand") as String
        }
        gradleMetrics["gradleVersion"] = project.gradle.gradleVersion
        return gradleMetrics

    }

    private fun hasProperty(property: String) = project.gradle.rootProject.hasProperty(property)
    private fun property(property: String) = project.property(property)
}