package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.metrics.PerformanceMetrics
import com.cdsap.talaiot.publisher.json.*
import com.cdsap.talaiot.util.Util.toBytes
import org.gradle.api.Project
import oshi.SystemInfo
import java.lang.System.currentTimeMillis
import java.lang.management.ManagementFactory.getOperatingSystemMXBean
import java.math.BigDecimal
import java.util.*
import java.util.regex.Pattern


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
        val perfMetrics = PerformanceMetrics(project).get()
        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val runtime = Runtime.getRuntime()
        val system = SystemInfo()
        val os = getOperatingSystemMXBean()
        val endMs = currentTimeMillis()
        // TODO: collect actual metrics
        return DetailedMetricsData(
            environment = Environment(
                cpuCount = runtime.availableProcessors(),
                osVersion = os.version,
                maxWorkers = 0,
                javaRuntime = runtime.toString(),
                javaVmName = System.getProperty("java.runtime.version"),
                javaXmsBytes = toBytes(perfMetrics["Xms"]),
                javaXmxBytes = toBytes(perfMetrics["Xmx"]),
                totalRamAvailableBytes = system.hardware.memory.total,
                locale = System.getProperty("user.language"),
                username = System.getProperty("user.name"),
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
            beginMs = beginMs,
            endMs = endMs,
            durationMs = endMs - beginMs,
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
