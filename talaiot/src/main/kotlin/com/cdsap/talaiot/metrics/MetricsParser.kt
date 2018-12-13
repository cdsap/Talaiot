package com.cdsap.talaiot.metrics

class MetricsParser(val metrics: List<Metrics>) : Metrics {
    override fun get(): Map<String, String> {
        val maps = mutableMapOf<String, String>()
        metrics.forEach {
            it.get().forEach {
                maps[it.key] = it.value
            }
        }
        return maps
    }
}
