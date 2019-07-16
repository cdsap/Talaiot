package com.cdsap.talaiot.metrics.base

import com.cdsap.talaiot.entities.ExecutionReport

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