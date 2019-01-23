package com.cdsap.talaiot.metrics

import org.gradle.BuildResult

class PerformanceMetrics(private val project: BuildResult) : Metrics {

    override fun get(): Map<String, String> {
        val runtime = Runtime.getRuntime()
        val runtimeMetrics = mapOf(
            "totalMemory" to "${runtime.totalMemory()}",
            "freeMemory" to "${runtime.freeMemory()}",
            "maxMemory" to "${runtime.maxMemory()}",
            "availableProcessors" to "${runtime.availableProcessors()}"
        )
        return runtimeMetrics + parseJvmArgs()
    }


    private fun parseJvmArgs(): Map<String, String> {
        if (project.gradle?.rootProject?.hasProperty("org.gradle.jvmargs") == true) {
            val listOfJvmArgs = mutableMapOf<String, String>()
            println(project.gradle?.rootProject?.property("org.gradle.jvmargs"))
            val properties: String = project.gradle?.rootProject?.property("org.gradle.jvmargs") as String
            println(properties)
            properties.split(" ").forEach {
                println(it)
                if (it.contains("Xmx")) {
                    listOfJvmArgs["Xmx"] = it.split("Xmx")[1]
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