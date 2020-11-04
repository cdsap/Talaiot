package com.cdsap.talaiot.plugin.rethinkdb

import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.rethinkdb.RethinkDbPublisher
import org.gradle.api.Project
import java.util.concurrent.Executors

class RethinkdbConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as RethinkdbExtension

        talaiotExtension.publishers?.apply {
            rethinkDbPublisher?.let { publisherConfig ->
                publishers.add(
                    RethinkDbPublisher(
                        publisherConfig,
                        LogTrackerImpl(talaiotExtension.logger),
                        Executors.newSingleThreadExecutor()
                    )
                )
            }
            publishers.addAll(customPublishers)
        }
        return publishers
    }
}
