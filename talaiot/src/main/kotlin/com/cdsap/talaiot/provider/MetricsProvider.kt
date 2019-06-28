package com.cdsap.talaiot.provider

import com.cdsap.talaiot.TalaiotExtension
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.*
import com.cdsap.talaiot.metrics.base.GradleMetric
import org.gradle.api.Project

/**
 * Provider for all metrics defined in the main MetricsConfiguration
 */
class MetricsProvider(
    /**
     * Gradle project required to access to the TalaiotExtension
     */
    private val project: Project
) : Provider<ExecutionReport> {

    /**
     * Aggregates all metrics depending if there are enabled in the MetricsConfiguration.
     * It access trough the Metrics interface
     *
     * @return collection of Pairs.
     */
    override fun get(): ExecutionReport {
        val report = ExecutionReport()

        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
        val simpleMetrics = mutableListOf<SimpleMetric<String>>()
            .apply {
                if (talaiotExtension.generateBuildId) {
                    add(BuildIdMetric())
                }

                if (talaiotExtension.metrics.gitMetrics) {
                    add(GitUserMetric())
                    add(GitBranchMetric())
                }

                add(UserMetric())
                add(OsMetric())

                talaiotExtension.metrics.customMetrics.forEach { metric ->
                    add(
                        SimpleMetric(
                            provider = { metric.value },
                            assigner = { report, value -> report.customProperties.properties[metric.key] = value }
                        )
                    )
                }
            }
            .forEach {
                it.get(Unit, report)
            }

        val gradleMetrics = mutableListOf<GradleMetric<String>>()
            .apply {
                add(RootProjectNameMetric())

                if (talaiotExtension.metrics.performanceMetrics) {
                    add(JvmXmsMetric())
                    add(JvmXmxMetric())
                    add(JvmMaxPermSizeMetric())
                }

                if (talaiotExtension.metrics.gradleMetrics) {
                    add(GradleVersionMetric())
                    add(GradleSwitchCachingMetric())
                    add(GradleSwitchDaemonMetric())
                    add(GradleSwitchParallelMetric())
                    add(GradleSwitchConfigureOnDemandMetric())
                }
            }.forEach {
                it.get(project, report)
            }

        return report
    }

//    var beginMs = System.currentTimeMillis()
//
//    /**
//     * Aggregates detailed metrics
//     * @return detailed metrics object
//     */
//    override fun get(): DetailedMetrics {
//        val perfMetrics = PerformanceMetrics(project).get()
//        val talaiotExtension = project.extensions.getByName("talaiot") as TalaiotExtension
//        val runtime = Runtime.getRuntime()
//        val system = SystemInfo()
//        val os = ManagementFactory.getOperatingSystemMXBean()
//        val endMs = System.currentTimeMillis()
//        // TODO: collect actual metrics
//        return DetailedMetricsData(
//            environment = Environment(
//                cpuCount = runtime.availableProcessors(),
//                osVersion = os.version,
//                maxWorkers = 0,
//                javaRuntime = runtime.toString(),
//                javaVmName = System.getProperty("java.runtime.version"),
//                javaXmsBytes = toBytes(perfMetrics["Xms"]),
//                javaXmxBytes = toBytes(perfMetrics["Xmx"]),
//                totalRamAvailableBytes = system.hardware.memory.total,
//                locale = System.getProperty("user.language"),
//                username = System.getProperty("user.name"),
//                publicIp = "",
//                defaultChartset = "",
//                ideVersion = "",
//                gradleVersion = "",
//
//                //Local/Remote
//                cacheMode = "",
//                cachePushEnabled = false,
//                cacheUrl = "",
//                cacheHit = 0,
//                cacheMiss = 0,
//                cacheStore = 0
//            ),
//            beginMs = beginMs,
//            endMs = endMs,
//            durationMs = endMs - beginMs,
//            customProperties = CustomProperties(
//                properties = mapOf()
//            ),
//            plugins = listOf(),
//            )
//    }
}
