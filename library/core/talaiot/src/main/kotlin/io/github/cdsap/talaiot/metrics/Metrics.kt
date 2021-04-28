package io.github.cdsap.talaiot.metrics

interface Metrics {

    val isCustom: Boolean
        get() = false

    fun toKey(): String
}