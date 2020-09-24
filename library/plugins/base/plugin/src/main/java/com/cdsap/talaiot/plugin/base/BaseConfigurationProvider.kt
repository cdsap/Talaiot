
import com.cdsap.talaiot.logger.LogTrackerImpl
import com.cdsap.talaiot.plugin.base.BaseExtension
import com.cdsap.talaiot.provider.PublisherConfigurationProvider
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.publisher.base.JsonPublisher
import com.cdsap.talaiot.publisher.base.OutputPublisher
import com.cdsap.talaiot.publisher.base.timeline.TimelinePublisher
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