package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutionReport
import org.gradle.api.Project

/**
 * [Metric] that wraps some gradle [Project.property]
 */
abstract class GradlePropertyMetric(val name: String, assigner: (ExecutionReport, String) -> Unit): GradleMetric<String>(
    provider = {
        when(it.gradle.rootProject.hasProperty(name)) {
            true -> it.gradle.rootProject.property(name) as? String ?: "undefined"
            else -> "undefined"
        }
    },
    assigner = assigner
)