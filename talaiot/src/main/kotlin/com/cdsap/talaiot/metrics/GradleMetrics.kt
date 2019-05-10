package com.cdsap.talaiot.metrics

import org.gradle.api.Project
import java.lang.IllegalStateException

/**
 * GradleMetrics provided for the builds.
 * Metrics included:
 *  -- gradleCaching
 *  -- gradleDaemon
 *  -- gradleParallel
 *  -- gradleConfigurationOnDemand
 *  -- gradleVersion
 *
 * It will change in the future to use the proper Gradle configuration.
 * check https://github.com/cdsap/Talaiot/issues/34
 *
 */
class GradleMetrics(
    /**
     * Gradle project required to access the properties
     */
    private val project: Project
) : Metrics {

    /**
     * Retrieve the values for the GradleMetrics defined
     *
     * @return collection of GradleMetrics
     */
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

    /**
     * Check if a property exists in the Gradle project given a property name
     * @param property name of the property to check
     * @return if the property is present in the configuration
     */
    private fun hasProperty(property: String) = project.gradle.rootProject.hasProperty(property)


    /**
     * Get property from the Gradle project given a property name
     * @param property name of the property to retrieve
     * @return value of the property
     */
    private fun property(property: String) = project.property(property)
}