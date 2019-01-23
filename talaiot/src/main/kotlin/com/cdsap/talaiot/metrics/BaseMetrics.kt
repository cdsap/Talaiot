package com.cdsap.talaiot.metrics

import org.gradle.BuildResult
import org.gradle.internal.os.OperatingSystem
import java.time.Instant
import kotlin.random.Random

class BaseMetrics(private val result: BuildResult) : Metrics {

    private val buildId =
        Instant.now().hashCode() + System.getProperty("user.name").trimSpaces().hashCode() + Random.nextInt().hashCode()

    override fun get() = mapOf(
        "user" to System.getProperty("user.name").trimSpaces(),
        "project" to (result.gradle?.rootProject?.name ?: "").trimSpaces(),
        "talaiotVersion" to "0.1.8.1",
        "buildId" to "$buildId",
        "os" to "${OperatingSystem.current().name}-${OperatingSystem.current().version}".trimSpaces()
    )

}

fun String.trimSpaces() = this.replace("\\s".toRegex(), "")
