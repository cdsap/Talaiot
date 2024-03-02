package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.metrics.process.JInfoProcess
import org.gradle.api.provider.Provider

class KotlinProcessMetrics(val value: Provider<String>) : SimpleMetric<Provider<String>>(provider = {
    value
}, assigner = { report, value ->
        val jInfoByProcess = JInfoProcess().parseJInfoData(value.get())
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
