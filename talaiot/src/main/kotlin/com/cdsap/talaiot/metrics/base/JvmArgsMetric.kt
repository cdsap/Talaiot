package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutionReport

abstract class JvmArgsMetric(val argProvider: (List<String>) -> String,
                             assigner: (ExecutionReport, String) -> Unit): GradleMetric<String>(
    provider = {
        if (it.gradle.rootProject.hasProperty("org.gradle.jvmargs")) {
            val properties: String = it.gradle.rootProject.property("org.gradle.jvmargs") as String
            argProvider(properties.split(" "))
        } else {
            "undefined"
        }
    },
    assigner = assigner
)