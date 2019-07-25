package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutionReport

/**
 * A generic metric representation
 *
 * Each metric has a return type [T] and a [Context] which is used to calculate the metric value
 */
abstract class Metric<T, in Context>(
    val provider: (Context) -> T,
    val assigner: (ExecutionReport, T) -> Unit
) {
    open fun get(context: Context, report: ExecutionReport) {
        val value = provider(context)
        assigner(report, value)
        value
    }
}