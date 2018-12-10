package com.cdsap.talaiot

import com.cdsap.talaiot.logger.LogTracking
import com.cdsap.talaiot.reporter.PublisherConfiguration
import org.gradle.api.Project


open class TalaiotExtension {
    var logger = LogTracking.Mode.INFO
    var track = ""
    var publisher: PublisherConfiguration? = null

    fun reporters(block: PublisherConfiguration.() -> Unit) {
        publisher = PublisherConfiguration().also(block)
    }
}