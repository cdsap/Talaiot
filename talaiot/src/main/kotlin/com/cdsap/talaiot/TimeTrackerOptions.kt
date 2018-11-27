package com.cdsap.talaiot

import okhttp3.OkHttpClient
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

object TimeTrackerOptions {
    fun Project.applyTimeTrackerOptions() = this.run {
      GlobalS
        GlobalScope
        if (System.getenv("TEAMCITY_VERSION") == null) {
            plugins.apply(TimeTrackerPlugin::class)

        }
    }
}
