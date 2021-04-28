
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.plugin.base.BaseExtension
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.Publisher
import io.github.cdsap.talaiot.publisher.JsonPublisher
import io.github.cdsap.talaiot.publisher.OutputPublisher
import io.github.cdsap.talaiot.publisher.timeline.TimelinePublisher
import org.gradle.api.Project

class BaseConfigurationProvider(
    val project: Project
) : PublisherConfigurationProvider {
    override fun get(): List<Publisher> {
        val publishers = mutableListOf<Publisher>()
        val talaiotExtension = project.extensions.getByName("talaiot") as BaseExtension
        talaiotExtension.publishers?.apply {
            outputPublisher?.apply {
                publishers.add(OutputPublisher(this, LogTrackerImpl(talaiotExtension.logger)))
            }
            if (jsonPublisher) {
                publishers.add(JsonPublisher(project.gradle))
            }

            if (timelinePublisher) {
                publishers.add(TimelinePublisher(project.gradle))
            }
            publishers.addAll(customPublishers)
        }
        return publishers

    }
}