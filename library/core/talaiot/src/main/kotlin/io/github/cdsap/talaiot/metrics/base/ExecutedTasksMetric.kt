package io.github.cdsap.talaiot.metrics.base

import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.github.cdsap.talaiot.entities.ExecutionReport

/**
 * [Metric] that operates on [ExecutedTasksInfo]
 */
abstract class ExecutedTasksMetric<T>(provider: (ExecutedTasksInfo) -> T, assigner: (ExecutionReport, T) -> Unit) : Metric<T, ExecutedTasksInfo>(provider, assigner)
