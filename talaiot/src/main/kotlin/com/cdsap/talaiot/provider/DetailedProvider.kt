package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.publisher.json.*
import org.gradle.api.Project
import java.lang.System.currentTimeMillis

/**
 * Provider for the detailed metrics
 */
class DetailedProvider(
    /**
     * Gradle project required to access to the TalaiotExtension
     */
    private val project: Project
) : Provider<DetailedMetrics> {

    var beginMs = currentTimeMillis()

    /**
     * Aggregates detailed metrics
     * @return detailed metrics object
     */
    override fun get(): DetailedMetrics {
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        // TODO: collect actual metrics
        return DetailedMetricsData(
            environment = Environment(
                cpuCount = 0,
                osVersion = "",
                maxWorkers = 0,
                javaRuntime = "",
                javaVmName = "",
                javaXmsBytes = 0,
                javaXmxBytes = 0,
                totalRamAvailableBytes = 0,
                locale = "",
                username = "",
                publicIp = "",
                defaultChartset = "",
                ideVersion = "",
                gradleVersion = "",

                //Local/Remote
                cacheMode = "",
                cachePushEnabled = false,
                cacheUrl = "",
                cacheHit = 0,
                cacheMiss = 0,
                cacheStore = 0
            ),
            beginMs = 0,
            endMs = 0,
            durationMs = 0,
            customProperties = CustomProperties(
                properties = mapOf()
            ),
            plugins = listOf(),
            switches = Switches(
                buildCache = false,
                configurationOnDemand = false,
                daemon = false,
                parallel = false,
                continueOnFailure = false,
                dryRun = false,
                offline = false,
                rerunTasks = false,
                refreshDependencies = false
            )
        )
    }
}
