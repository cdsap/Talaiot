package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutionReport
import org.gradle.api.Project

/**
 * [Metric] that operates on [Project]
 */
abstract class GradleMetric<T>(provider: (Project) -> T, assigner: (ExecutionReport, T) -> Unit): Metric<T, Project>(provider, assigner)