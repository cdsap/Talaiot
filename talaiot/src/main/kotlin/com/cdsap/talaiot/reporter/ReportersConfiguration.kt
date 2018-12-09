package com.cdsap.talaiot.reporter

import com.cdsap.talaiot.reporter.ReporterConfiguration

class ReportersConfiguration {
    var reporters = mutableListOf<ReporterConfiguration>()
    var ask: String = ""

    fun reporters(listOfReporters: List<ReporterConfiguration>) {
        reporters.addAll(listOfReporters)
    }


}