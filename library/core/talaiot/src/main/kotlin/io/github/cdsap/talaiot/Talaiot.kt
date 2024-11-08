package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.extensions.isCompatibleWithIsolatedProjects
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.MetricsProvider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.TalaiotPublisher
import io.github.cdsap.talaiot.publisher.TalaiotPublisherImpl
import io.github.cdsap.talaiot.util.ConfigurationPhaseObserver
import io.github.cdsap.valuesourceprocess.CommandLineWithOutputValue
import io.github.cdsap.valuesourceprocess.jInfo
import io.github.cdsap.valuesourceprocess.jStat
import org.gradle.api.Project
import org.gradle.api.configuration.BuildFeatures
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import org.gradle.internal.extensions.core.serviceOf
import org.gradle.util.GradleVersion

/**
 * Talaiot main [Plugin].
 *
 * Talaiot is a simple and extensible plugin for teams that use Gradle Build System. It stores information about
 * your Gradle tasks and helps you detect problems and bottlenecks of your builds. For every tracked task and build
 * it will add additional information defined by default and custom metrics
 * specified in [io.github.cdsap.talaiot.configuration.MetricsConfiguration].
 *
 * usage:
 * plugins {
 *   id("talaiot")
 * }
 */
class Talaiot<T : TalaiotExtension>(
    private val classExtension: Class<T>,
    private val publisherConfigurationProvider: PublisherConfigurationProvider
) {
    /**
     * Initialization of the plugin.
     *
     * @param project Gradle project used to to retrieve buildProperties and build information.
     */

    fun setUpPlugin(target: Project) {
        val extension = target.extensions.create("talaiot", classExtension, target)
        val executionReport = ExecutionReport()
        val startTime = System.currentTimeMillis()
        target.gradle.taskGraph.whenReady {
            val dictionary = if (GradleVersion.current().isCompatibleWithIsolatedProjects() && target.serviceOf<BuildFeatures>().isolatedProjects.active.getOrElse(false)) emptyMap<String, String>() else it.allTasks.associate { it.path to it.javaClass.toString().replace("class ", "").replace("_Decorated", "") }

            val parameters = target.gradle.startParameter.taskRequests.flatMap {
                it.args.flatMap { task ->
                    listOf(task.toString())
                }
            }
            populateMetrics(executionReport, target, extension.metrics)
            val talaiotPublisher = createTalaiotPublisher(extension, executionReport)
            val configurationProvider = target.providers.of(ConfigurationPhaseObserver::class.java) { }

            ConfigurationPhaseObserver.init()

            // ValueSources invoked as metrics invalidate the configuration cache when the value changes.
            // https://github.com/cdsap/Talaiot/issues/408
            // To avoid this issue we need to create the provider that wouldn't be retrieved
            // until the publishing phase and if the metric is enabled
            val gitBranch = target.providers.of(CommandLineWithOutputValue::class.java) {
                it.parameters.commands.set("git rev-parse --abbrev-ref HEAD")
            }

            val serviceProvider: Provider<TalaiotBuildService> =
                target.gradle.sharedServices.registerIfAbsent(
                    "talaiotService",
                    TalaiotBuildService::class.java
                ) { spec ->
                    spec.parameters.publisher.set(talaiotPublisher)
                    spec.parameters.initTime.set(startTime)
                    spec.parameters.startParameters.set(parameters)
                    spec.parameters.customPublishers.set(publisherConfigurationProvider.get())
                    spec.parameters.publishOnNewThread.set(extension.publishOnNewThread)
                    spec.parameters.configurationPhaseExecuted.set(configurationProvider)
                    spec.parameters.jstatGradle = target.jStat("GradleDaemon")
                    spec.parameters.jstatKotlin = target.jStat("KotlinCompileDaemon")
                    spec.parameters.jInfoGradle = target.jInfo("GradleDaemon")
                    spec.parameters.jInfoKotlin = target.jInfo("KotlinCompileDaemon")
                    spec.parameters.dictionary.set(dictionary)
                    spec.parameters.processes.set(extension.metrics.processMetrics)
                    spec.parameters.gitBranchMetric = gitBranch
                    spec.parameters.processGitBranchMetric.set(extension.metrics.gitMetrics)
                }
            target.serviceOf<BuildEventsListenerRegistry>().onTaskCompletion(serviceProvider)
        }
    }

    private fun populateMetrics(executionReport: ExecutionReport, target: Project, metrics: MetricsConfiguration) {
        MetricsProvider(metrics.build(target), executionReport, target).get()
    }

    private fun createTalaiotPublisher(
        extension: T,
        executionReport: ExecutionReport
    ): TalaiotPublisher {
        val logger = LogTrackerImpl(LogTracker.Mode.INFO)
        val taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
        val buildFilterProcessor = BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())
        return TalaiotPublisherImpl(executionReport, taskFilterProcessor, buildFilterProcessor)
    }
}
