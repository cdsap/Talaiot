package com.cdsap.talaiot.plugin.elasticsearch

import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import org.gradle.api.Project
import java.util.concurrent.Executors

class ElasticSearchConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as ElasticSearchExtension

        talaiotExtension.publishers?.apply {
            publishers.add(
                ElasticSearchPublisher(
                    this.elasticSearchPublisher!!,
                    LogTrackerImpl(talaiotExtension.logger),
                    Executors.newSingleThreadExecutor()
                )
            )
            publishers.addAll(customPublishers)
        }
        return publishers

    }
}