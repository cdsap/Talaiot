package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.metrics.process.JInfoProcess

class KotlinProcessMetrics(val value: String) : SimpleMetric<String>(provider = {
    value
}, assigner = { report, value ->
        val jInfoByProcess = JInfoProcess().parseJInfoData(value)
        if (jInfoByProcess.isNotEmpty()) {
            report.environment.kotlinProcessesAvailable = jInfoByProcess.size
            report.environment.multipleKotlinProcesses = jInfoByProcess.size > 1
            if (jInfoByProcess.size == 1) {
                report.environment.kotlinJvmArgs = jInfoByProcess.entries.first().value
            } else {
                report.environment.multipleKotlinJvmArgs = jInfoByProcess
            }
        }
    })
