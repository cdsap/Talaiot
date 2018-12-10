package com.cdsap.talaiot

import com.cdsap.talaiot.reporter.PublisherConfiguration
import org.gradle.api.Project


open class TalaiotExtension(val project: Project) {
    var default = false
    var track = ""
    var publisher: PublisherConfiguration? = null

    fun reporters(block: PublisherConfiguration.() -> Unit) {
        publisher = PublisherConfiguration().also(block)
    }
}