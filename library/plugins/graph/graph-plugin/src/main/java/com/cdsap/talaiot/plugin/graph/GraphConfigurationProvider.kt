package com.cdsap.talaiot.plugin.graph

import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.graph.GraphPublisherFactoryImpl
import com.cdsap.talaiot.publisher.graph.TaskDependencyGraphPublisher
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
