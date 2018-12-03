package com.cdsap.talaiot

import com.cdsap.talaiot.reporter.Reporter
import org.gradle.api.Project


class TalaiotExtension @JvmOverloads constructor(
        @get:Internal internal val name: String = "default" // Needed for Gradle
) {

    @get:Input
    var track
        get() = trackOrDefault.publishedName
        set(value) {
            _track = validatedTrack(value)
        }

}