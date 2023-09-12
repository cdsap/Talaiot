package io.github.cdsap.talaiot.metrics.base

import io.github.cdsap.talaiot.entities.ExecutionReport

/**
 * [Metric] that operates on some command line action output
 */
open class CmdMetric(val cmd: String, assigner: (ExecutionReport, String) -> Unit) : GradleMetric<String>(
    provider = { project ->
        try {
            project.providers.exec { spec ->
                spec.commandLine(cmd.split(" "))
            }.standardOutput.asText.get().trim()
        } catch (e: IllegalStateException) {
            throw IllegalArgumentException(
                "Error executing $cmd. Consider disabling the metric from your configuration", e
            )
        }
    },
    assigner = assigner
)
