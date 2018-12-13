package com.cdsap.talaiot.metrics

class PerformanceMetrics() : Metrics {
    override fun get(): Map<String, String> {
        val runtime = Runtime.getRuntime()
        val nas = mutableMapOf<String, String>()
        nas["totalMemory"] = "${runtime.totalMemory()}"
        nas["freeMemory"] = "${runtime.freeMemory()}"
        nas["maxMemory"] = "${runtime.maxMemory()}"
        nas["availableProcessors"] = "${runtime.availableProcessors()}"
        return nas
    }
}