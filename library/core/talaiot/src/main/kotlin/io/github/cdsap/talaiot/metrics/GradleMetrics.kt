package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.extensions.gradleVersionCompatibleWithIsolatedProjects
import io.github.cdsap.talaiot.extensions.toBytes
import io.github.cdsap.talaiot.metrics.base.GradleMetric
import io.github.cdsap.talaiot.metrics.base.JvmArgsMetric
import io.github.cdsap.talaiot.util.TaskAbbreviationMatcher
import io.github.cdsap.talaiot.util.TaskName
import org.gradle.api.Project
import org.gradle.api.configuration.BuildFeatures
import org.gradle.api.internal.StartParameterInternal
import org.gradle.api.invocation.Gradle
import org.gradle.internal.extensions.core.serviceOf
import org.gradle.util.GradleVersion

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

class UserMetric : GradleMetric<String>(
    provider = { project: Project -> project.providers.systemProperty("user.name").get() },
    assigner = { report, value -> report.environment.username = value }
)

class LocaleMetric : GradleMetric<String>(
    provider = { project: Project ->
        project.providers.systemProperty("user.language").get()
    },
    assigner = { report, value -> report.environment.locale = value }
)

private fun Gradle.findRequestedTasks(): List<String> {
    val taskNames = startParameter.taskNames
    return if (GradleVersion.current().version.gradleVersionCompatibleWithIsolatedProjects() && rootProject.serviceOf<BuildFeatures>().isolatedProjects.active.getOrElse(false)) {
        taskNames
    } else {
        val executedTasks = taskGraph.allTasks.map { TaskName(name = it.name, path = it.path) }
        val taskAbbreviationHandler = TaskAbbreviationMatcher(executedTasks)
        taskNames.map {
            taskAbbreviationHandler.findRequestedTask(it)
        }
    }
}
