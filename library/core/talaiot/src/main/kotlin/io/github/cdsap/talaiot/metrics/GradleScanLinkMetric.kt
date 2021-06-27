package io.github.cdsap.talaiot.metrics

import com.gradle.scan.plugin.BuildScanExtension
import com.gradle.scan.plugin.PublishedBuildScan
import io.github.cdsap.talaiot.metrics.base.BuildResultMetric
import org.gradle.api.Action
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.enterprise.core.GradleEnterprisePluginManager
import org.gradle.internal.logging.LoggingManagerInternal
import org.gradle.internal.logging.events.OutputEvent
import org.gradle.internal.logging.events.StyledTextOutputEvent
import org.gradle.util.GradleVersion

/**
 * Be warned: super hacky
 *
 * The metrics upload and link creation happens after the build actually already finishes in the
 * official lifecycle [org.gradle.BuildListener]
 */
class GradleScanLinkMetric : BuildResultMetric<String?>(
    provider = { result ->
        val gradleInternal = result.gradle as GradleInternal
        val services = gradleInternal.services

        when {
            GradleVersion.current() >= GradleVersion.version("6.0") &&
                GradleVersion.current() <= GradleVersion.version("6.5.1") -> {
                classFor("org.gradle.internal.scan.eob.DefaultBuildScanEndOfBuildNotifier")?.let {
                    services.get(it)
                }?.let { endOfBuildNotifier ->
                    val loggingManager: LoggingManagerInternal =
                        services.get(LoggingManagerInternal::class.java)
                    var shouldCaptureNext = false
                    var link: String? = null
                    val listener: (OutputEvent) -> Unit = {
                        if (it is StyledTextOutputEvent) {
                            if (it.spans.any { span -> span.text.contains("Publishing build scan") }) {
                                shouldCaptureNext = true
                            } else if (shouldCaptureNext) {
                                shouldCaptureNext = false
                                link = it.spans.map { it.text }.joinToString(separator = "").trim()
                            }
                        }
                    }
                    loggingManager.addOutputEventListener(listener)

                    val fireBuildCompleteMethod =
                        endOfBuildNotifier::class.java.getDeclaredMethod(
                            "fireBuildComplete",
                            Throwable::class.java
                        )
                    fireBuildCompleteMethod.invoke(endOfBuildNotifier, result.failure)

                    val listenerField = endOfBuildNotifier::class.java.getDeclaredField("listener")
                    listenerField.isAccessible = true
                    listenerField.set(endOfBuildNotifier, null)

                    loggingManager.removeOutputEventListener(listener)

                    link
                } ?: null
            }
            GradleVersion.current() >= GradleVersion.version("6.7") -> {
                try {
                    val gradleEnterprisePluginManager: GradleEnterprisePluginManager =
                        services.get(GradleEnterprisePluginManager::class.java) as GradleEnterprisePluginManager
                    val project = gradleInternal.rootProject
                    val buildScanExtension =
                        project.extensions.getByType(BuildScanExtension::class.java)
                    val scanLinkListener = GradleScanLinkListener()

                    buildScanExtension.buildScanPublished(scanLinkListener)
                    gradleEnterprisePluginManager.buildFinished(result.failure)

                    val adapterField =
                        gradleEnterprisePluginManager::class.java.getDeclaredField("adapter")
                    adapterField.isAccessible = true
                    adapterField.set(gradleEnterprisePluginManager, null)

                    scanLinkListener.scanLink
                } catch (e: UnknownDomainObjectException) {
                    null
                }
            }
            else -> {
                /**
                 * Previously the code registered a regular buildListener
                 * This is unsupported currently
                 */

                null
            }
        }
    },
    assigner = { report, value ->
        report.scanLink = value
    }
)

private fun classFor(name: String): Class<*>? {
    return try {
        Class.forName(name)
    } catch (e: ClassNotFoundException) {
        null
    }
}

class GradleScanLinkListener(override var scanLink: String? = null) : GradleScanLinkAccumulator {
    override fun execute(publishedBuildScan: PublishedBuildScan) {
        scanLink = publishedBuildScan.buildScanUri.toString()
    }
}

interface GradleScanLinkAccumulator : Action<PublishedBuildScan> {
    val scanLink: String?
}
