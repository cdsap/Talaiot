package com.cdsap.talaiot.metrics

class PerformanceMetrics : Metrics {
    override fun get(): Map<String, String> {
        val runtime = Runtime.getRuntime()
        return mapOf(
            "totalMemory" to "${runtime.totalMemory()}",
            "freeMemory" to "${runtime.freeMemory()}",
            "maxMemory" to "${runtime.maxMemory()}",
            "availableProcessors" to "${runtime.availableProcessors()}"
        )
    }
}