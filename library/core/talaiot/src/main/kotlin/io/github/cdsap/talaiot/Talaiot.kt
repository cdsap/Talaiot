package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.filter.BuildFilterProcessor
import io.github.cdsap.talaiot.filter.TaskFilterProcessor
import io.github.cdsap.talaiot.logger.LogTracker
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.MetricsPostBuildProvider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.TalaiotPublisherImpl
import org.gradle.api.Project
import org.gradle.api.provider.Provider
import org.gradle.build.event.BuildEventsListenerRegistry
import org.gradle.configurationcache.extensions.serviceOf

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
     * Initialization of the plugin. The plugin needs to receive callbacks
     * from the [org.gradle.api.execution.TaskExecutionListener]
     * and [org.gradle.BuildListener] to start tracking the information of the tasks.
     *
     * Additionally we need the a list of metrics and providers that will be used during the execution.
     *
     * @param extension Talaiot extension that contains the configuration
     * @param project Gradle project used to to retrieve buildProperties and build information.
     */

    fun setUpPlugin(target: Project) {

        val extension = target.extensions.create("talaiot", classExtension, target)
        val executionReport = ExecutionReport()

        target.gradle.taskGraph.whenReady {
            val parameters = target.gradle.startParameter.taskRequests.flatMap {
                it.args.flatMap { task ->
                    listOf(task.toString())
                }
            }
            MetricsPostBuildProvider(MetricsConfiguration().build(), executionReport, target).get()
            val logger = LogTrackerImpl(LogTracker.Mode.INFO)
            val taskFilterProcessor = TaskFilterProcessor(logger, extension.filter)
            val buildFilterProcessor =
                BuildFilterProcessor(logger, extension.filter?.build ?: BuildFilterConfiguration())
            publisherConfigurationProvider.get()

            val talaiotPublisher = TalaiotPublisherImpl(
                executionReport, publisherConfigurationProvider.get(), taskFilterProcessor, buildFilterProcessor
            )

            val serviceProvider: Provider<TalaiotBuildService> = target.gradle.sharedServices.registerIfAbsent(
                "talaiotService", TalaiotBuildService::class.java
            ) { spec ->
                // Provide some parameters
                spec.parameters.publisher.set(talaiotPublisher)
                spec.parameters.startParameters.set(parameters)
            }
            target.serviceOf<BuildEventsListenerRegistry>().onTaskCompletion(serviceProvider)
        }
    }
}
