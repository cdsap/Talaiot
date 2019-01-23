package com.cdsap.talaiot.metrics

import org.gradle.BuildResult

class GradleMetrics(private val buildResults: BuildResult) : Metrics {

    override fun get(): Map<String, String> {
        val gradleMetrics = mutableMapOf<String, String>()

        if (buildResults.gradle?.rootProject?.hasProperty("org.gradle.caching") == true) {
            gradleMetrics["gradleCaching"] = buildResults.gradle?.rootProject?.property("org.gradle.caching") as String
        }
        if (buildResults.gradle?.rootProject?.hasProperty("org.gradle.daemon") == true) {
            gradleMetrics["gradleDaemon"] = buildResults.gradle?.rootProject?.property("org.gradle.daemon") as String
        }
        if (buildResults.gradle?.rootProject?.hasProperty("org.gradle.parallel") == true) {
            gradleMetrics["gradleParallel"] =
                    buildResults.gradle?.rootProject?.property("org.gradle.parallel") as String
        }
        if (buildResults.gradle?.rootProject?.hasProperty("org.gradle.configureondemand") == true) {
            gradleMetrics["gradleConfigurationOnDemand"] =
                    buildResults.gradle?.rootProject?.property("org.gradle.configureondemand") as String
        }
        gradleMetrics["gradleVerion"] = buildResults.gradle?.gradleVersion ?: ""
        return gradleMetrics

    }
}