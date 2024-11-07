package io.github.cdsap.talaiot.publisher

import io.github.cdsap.jdk.tools.parser.ConsolidateProcesses
import io.github.cdsap.jdk.tools.parser.model.TypeProcess
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.entities.Processes
import io.github.cdsap.talaiot.entities.TaskLength
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.metrics.GradleProcessMetrics
import io.github.cdsap.talaiot.metrics.KotlinProcessMetrics

/**
 * Implementation of [TalaiotPublisher].
 * Once the [TalaiotBuildService] is closed we need to publish the build information based on the configuration.
 * This configuration is composed by:
 *   - Filtering task
 *   - Build Publishing Filter
 * Finally, it completes the [ExecutionReport] information with the general build info and
 * publishes the build information with the publishers provided
 */
class TalaiotPublisherImpl(
    private val executionReport: ExecutionReport,
    private val taskFilterProcessor: TaskFilterProcessor,
    private val buildFilterProcessor: BuildFilterProcessor
) : TalaiotPublisher, java.io.Serializable {

    override fun publish(
        taskLengthList: MutableList<TaskLength>,
        start: Long,
        configuration: Long,
        end: Long,
        success: Boolean,
        duration: Long,
        publisherProvider: List<Publisher>,
        configurationCacheHit: Boolean,
        gradleStat: String,
        kotlinStat: String,
        gradleInfo: String,
        kotlinInfo: String,
        processProcessMetrics: Boolean
    ) {
        executionReport.tasks = taskLengthList.filter { taskFilterProcessor.taskLengthFilter(it) }
        executionReport.unfilteredTasks = taskLengthList
        executionReport.beginMs = start.toString()
        executionReport.endMs = end.toString()
        executionReport.success = success
        executionReport.executionDurationMs = duration.toString()
        executionReport.durationMs = (duration + configuration).toString()
        executionReport.configurationDurationMs = configuration.toString()
        executionReport.configurationCacheHit = configurationCacheHit

        if (buildFilterProcessor.shouldPublishBuild(executionReport)) {
            if (processProcessMetrics) {
                val processesKotlin = ConsolidateProcesses().consolidate(kotlinStat, kotlinInfo, TypeProcess.Kotlin)
                val processesGradle = ConsolidateProcesses().consolidate(gradleStat, gradleInfo, TypeProcess.Gradle)
                executionReport.environment.processesStats = Processes(
                    listKotlinProcesses = processesKotlin,
                    listGradleProcesses = processesGradle
                )
                GradleProcessMetrics(gradleInfo).get(Unit, executionReport)
                KotlinProcessMetrics(kotlinInfo).get(Unit, executionReport)
            }
            publisherProvider.forEach {
                it.publish(executionReport)
            }
        }
    }
}
