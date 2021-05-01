package io.github.cdsap.talaiot.metrics.base

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.SimpleMetric
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.IllegalStateException

/**
 * [Metric] that operates on some command line action output
 */
open class CmdMetric(val cmd: String, assigner: (ExecutionReport, String) -> Unit) : SimpleMetric<String>(
    provider = {
        val runtime = Runtime.getRuntime()
        try {
            val reader = BufferedReader(
                InputStreamReader(runtime.exec(cmd).inputStream)
            )
            val result = reader.readLine()
            result ?: "undefined"
        } catch (e: IllegalStateException) {
            throw IllegalArgumentException("Error executing $cmd. Consider disabling the metric from your configuration", e)
        }
    },
    assigner = assigner
)
