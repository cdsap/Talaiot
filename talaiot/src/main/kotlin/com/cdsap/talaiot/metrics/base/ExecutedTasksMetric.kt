package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutedTasksInfo
import com.cdsap.talaiot.entities.ExecutionReport

/**
 * [Metric] that operates on [ExecutedTasksInfo]
 */
abstract class ExecutedTasksMetric<T>(provider: (ExecutedTasksInfo) -> T, assigner: (ExecutionReport, T) -> Unit): Metric<T, ExecutedTasksInfo>(provider, assigner)
