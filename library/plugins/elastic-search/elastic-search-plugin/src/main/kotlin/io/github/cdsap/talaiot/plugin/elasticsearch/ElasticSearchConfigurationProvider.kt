package io.github.cdsap.talaiot.plugin.elasticsearch

import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.elasticsearch.ElasticSearchPublisher
import org.gradle.api.Project

class ElasticSearchConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as ElasticSearchExtension

        talaiotExtension.publishers?.apply {
            elasticSearchPublisher?.let { publisherConfig ->
                publishers.add(
                    ElasticSearchPublisher(
                        publisherConfig,
                        LogTrackerImpl(talaiotExtension.logger)
                    )
                )
            }
        }
        return publishers
    }
}
