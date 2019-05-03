package com.cdsap.talaiot.metrics

import org.gradle.api.Project

class PerformanceMetrics(
    private val project: Project
) : Metrics {

    override fun get(): Map<String, String> {
        val runtime = Runtime.getRuntime()
        val runtimeMetrics = mapOf(
            "availableProcessors" to "${runtime.availableProcessors()}"
        )
        return runtimeMetrics + parseJvmArgs()
    }


    private fun parseJvmArgs(): Map<String, String> {
        if (project.gradle.rootProject.hasProperty("org.gradle.jvmargs")) {
            val listOfJvmArgs = mutableMapOf<String, String>()
            val properties: String = project.gradle.rootProject.property("org.gradle.jvmargs") as String
            properties.split(" ").forEach {
                if (it.contains("Xmx")) {
                    listOfJvmArgs["Xmx"] = it.split("Xmx")[1]
                }
                if (it.contains("Xms")) {
                    listOfJvmArgs["Xms"] = it.split("Xms")[1]
                }
                if (it.contains("MaxPermSize")) {
                    listOfJvmArgs["MaxPermSize"] = it.split("=")[1]
                }
            }
            return listOfJvmArgs
        } else {
            return emptyMap()
        }
    }
}