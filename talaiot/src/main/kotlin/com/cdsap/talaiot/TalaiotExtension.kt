package com.cdsap.talaiot

import com.cdsap.talaiot.reporter.Reporter
import org.gradle.api.Project
import org.gradle.api.tasks.Internal


open class TalaiotExtension (val project: Project) {


    var track = ""
    var urlGraphana = ""
    var metric = ""

}