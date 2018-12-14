package com.cdsap.talaiot.metrics

class MetricsParser(val metrics: List<Metrics>) : Metrics {
    override fun get(): Map<String, String> = metrics.fold(mutableMapOf()) { acc, f ->
        (f.get().toMutableMap() + acc.toMutableMap()) as MutableMap<String, String>
    }
}
