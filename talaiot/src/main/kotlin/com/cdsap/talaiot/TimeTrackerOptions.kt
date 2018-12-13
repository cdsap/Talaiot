package com.cdsap.talaiot

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

object TimeTrackerOptions {
    fun Project.applyTimeTrackerOptions() = this.run {
        if (System.getenv("TEAMCITY_VERSION") == null) {
            plugins.apply(TalaiotPlugin::class)

        }
    }
}
