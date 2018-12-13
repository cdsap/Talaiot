package com.cdsap.talaiot.metrics

interface Metrics {
    fun get(): Map<String, String>
}