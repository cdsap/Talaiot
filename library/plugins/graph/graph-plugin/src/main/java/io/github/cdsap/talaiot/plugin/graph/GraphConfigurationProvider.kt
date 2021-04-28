package io.github.cdsap.talaiot.plugin.graph

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.graph.GraphPublisherFactoryImpl
import io.github.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher
import org.gradle.api.Project
import java.util.concurrent.Executors

class GraphConfigurationProvider(
    private val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as GraphExtension

        talaiotExtension.publishers?.apply {
            taskDependencyGraphPublisher?.let { publisherConfig ->
                publishers.add(
                    TaskDependencyGraphPublisher(
                        publisherConfig,
                        LogTrackerImpl(talaiotExtension.logger),
                        Executors.newSingleThreadExecutor(),
                        GraphPublisherFactoryImpl()
                    )
                )
            }
            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
