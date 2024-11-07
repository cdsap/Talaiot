package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.metrics.process.JInfoProcess

class GradleProcessMetrics(val value: String) : SimpleMetric<String>(provider = {
    value
}, assigner = { report, value ->
        val jInfoByProcess = JInfoProcess().parseJInfoData(value)
        if (jInfoByProcess.isNotEmpty()) {
            report.environment.gradleProcessesAvailable = jInfoByProcess.size
            report.environment.multipleGradleProcesses = jInfoByProcess.size > 1
            if (jInfoByProcess.size == 1) {
                report.environment.gradleJvmArgs = jInfoByProcess.entries.first().value
            } else {
                report.environment.multipleGradleJvmArgs = jInfoByProcess
            }
        }
    })
