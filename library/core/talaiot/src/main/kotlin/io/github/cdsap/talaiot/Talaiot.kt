package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.provider.MetricsPreBuildProvider
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
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

        println("111111222")
        val extension = target.extensions.create("talaiot", classExtension, target)
        val buildOperationListener = BuildCacheOperationListener()
        val executionReport = ExecutionReport()

        val serviceProvider: Provider<TalaiotBuildService> = target.getGradle().getSharedServices().registerIfAbsent(
            "web",
            TalaiotBuildService::class.java
        ) { spec ->
            // Provide some parameters
        //    spec.getParameters().target().set(5005)
        }
        target.serviceOf<BuildEventsListenerRegistry>().onTaskCompletion(serviceProvider)

//        //   target.gradle.taskGraph.whenReady {
//            val metrics = extension.metrics.build()
//            val executionReportWithMetricsPreBuildPopulated =
//                MetricsPreBuildProvider(target, metrics, executionReport).get()
//println("inakiiaia")
//            val listener = TalaiotListener(
//                target,
//                extension,
//                buildOperationListener,
//                publisherConfigurationProvider,
//                metrics,
//                executionReportWithMetricsPreBuildPopulated
//            )
//            target.gradle.addBuildListener(listener)
//            target.gradle.buildOperationListenerManager().addListener(buildOperationListener)
       // }
    }
}
