
import io.github.cdsap.talaiot.logger.LogTrackerImpl
import io.github.cdsap.talaiot.plugin.base.BaseExtension
import io.github.cdsap.talaiot.provider.PublisherConfigurationProvider
import io.github.cdsap.talaiot.publisher.JsonPublisher
import io.github.cdsap.talaiot.publisher.OutputPublisher
import io.github.cdsap.talaiot.publisher.Publisher
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
                publishers.add(JsonPublisher(project.gradle.rootProject.buildDir))
            }
        }
        return publishers
    }
}
