package com.cdsap.talaiot

import com.cdsap.talaiot.reporter.Reporter
import org.gradle.api.Project


class TalaiotExtension(project: Project) {
    val reporters: List<Reporter> = emptyList()

}