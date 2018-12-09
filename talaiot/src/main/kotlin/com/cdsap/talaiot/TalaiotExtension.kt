package com.cdsap.talaiot

import com.cdsap.talaiot.reporter.Reporter
import org.gradle.api.Project
import org.gradle.api.tasks.Internal
import com.cdsap.talaiot.reporter.ReportersConfiguration


open class TalaiotExtension(val project: Project) {
    var default = false
    var track = ""
    var reporters: ReportersConfiguration? = null

    fun reporters(block: ReportersConfiguration.() -> Unit) {
        reporters = ReportersConfiguration().also(block)
    }
}