package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension


class CustomMetrics(private val talaiotExtension: TalaiotExtension) : Metrics {

    override fun get() = talaiotExtension.customMetrics
}
