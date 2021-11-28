package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.extensions.toBytes
import io.github.cdsap.talaiot.metrics.base.GradleMetric
import io.github.cdsap.talaiot.metrics.base.JvmArgsMetric
import io.github.cdsap.talaiot.util.TaskAbbreviationMatcher
import io.github.cdsap.talaiot.util.TaskName
import org.gradle.api.internal.StartParameterInternal
import org.gradle.api.internal.project.DefaultProject
import org.gradle.api.invocation.Gradle
import org.gradle.launcher.daemon.server.scaninfo.DaemonScanInfo

class RootProjectNameMetric : GradleMetric<String>(
    provider = { it.gradle.rootProject.name },
    assigner = { report, value -> report.rootProject = value }
)

class GradleVersionMetric : GradleMetric<String>(
    provider = { it.gradle.gradleVersion },
    assigner = { report, value -> report.environment.gradleVersion = value }
)

class GradleSwitchCachingMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isBuildCacheEnabled.toString() },
    assigner = { report, value -> report.environment.switches.buildCache = value }
)

class GradleSwitchBuildScanMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isBuildScan.toString() },
    assigner = { report, value -> report.environment.switches.buildScan = value }
)

class GradleSwitchConfigureOnDemandMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isConfigureOnDemand.toString() },
    assigner = { report, value -> report.environment.switches.configurationOnDemand = value }
)

class GradleSwitchParallelMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isParallelProjectExecutionEnabled.toString() },
    assigner = { report, value -> report.environment.switches.parallel = value }
)

class GradleSwitchRerunTasksMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isRerunTasks.toString() },
    assigner = { report, value -> report.environment.switches.rerunTasks = value }
)

class GradleSwitchDryRunMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isDryRun.toString() },
    assigner = { report, value -> report.environment.switches.dryRun = value }
)

class GradleSwitchOfflineMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isOffline.toString() },
    assigner = { report, value -> report.environment.switches.offline = value }
)

class GradleSwitchRefreshDependenciesMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.isRefreshDependencies.toString() },
    assigner = { report, value -> report.environment.switches.refreshDependencies = value }
)

class GradleSwitchDaemonMetric : GradleMetric<String?>(
    provider = {
        val daemonScanInfo: DaemonScanInfo? =
            (it.rootProject as DefaultProject).services.get(DaemonScanInfo::class.java)
        daemonScanInfo?.isSingleUse?.toString()
    },
    assigner = { report, value -> report.environment.switches.daemon = value }
)

class GradleSwitchConfigurationCacheMetric : GradleMetric<String?>(
    provider = {
        try {
            (it.gradle.startParameter as StartParameterInternal).configurationCache.get().toString()
        } catch (e: NoSuchMethodError) {
            ""
        }
    },
    assigner = { report, value -> report.environment.switches.configurationCache = value }
)

class GradleMaxWorkersMetric : GradleMetric<String>(
    provider = { it.gradle.startParameter.maxWorkerCount.toString() },
    assigner = { report, value -> report.environment.maxWorkers = value }
)

class JvmXmxMetric : JvmArgsMetric(
    argProvider = { paramList: List<String> ->
        val xmxParam = paramList.find { param -> param.contains("Xmx") }
        xmxParam?.split("Xmx")?.get(1)?.toBytes()
    },
    assigner = { report, value -> report.environment.javaXmxBytes = value }
)

class JvmXmsMetric : JvmArgsMetric(
    argProvider = { paramList: List<String> ->
        val xmsParam = paramList.find { param -> param.contains("Xms") }
        xmsParam?.split("Xms")?.get(1)?.toBytes()
    },
    assigner = { report, value -> report.environment.javaXmsBytes = value }
)

class JvmMaxPermSizeMetric : JvmArgsMetric(
    argProvider = { paramList: List<String> ->
        val maxPermSize = paramList.find { param -> param.contains("MaxPermSize") }
        maxPermSize?.split("=")?.get(1)?.toBytes()
    },
    assigner = { report, value -> report.environment.javaMaxPermSize = value }
)

class GradleRequestedTasksMetric : GradleMetric<String>(
    provider = { project ->
        val taskNames = project.gradle.findRequestedTasks()
        if (taskNames.all { it.endsWith("generateDebugSources") }) {
            "gradleSync"
        } else {
            taskNames.joinToString(separator = " ")
        }
    },
    assigner = { report, value -> report.requestedTasks = value }
)

private fun Gradle.findRequestedTasks(): List<String> {
    try {
        val taskNames = startParameter.taskNames
        val executedTasks = taskGraph.allTasks.map { TaskName(name = it.name, path = it.path) }
        val taskAbbreviationHandler = TaskAbbreviationMatcher(executedTasks)
        return taskNames.map {
            taskAbbreviationHandler.findRequestedTask(it)
        }
    } catch (e: Exception) {
        return emptyList<String>()
    }
}
