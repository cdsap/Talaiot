package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.metrics.base.BuildResultMetric
import com.gradle.scan.plugin.BuildScanExtension
import com.gradle.scan.plugin.PublishedBuildScan
import org.gradle.api.Action
import org.gradle.api.UnknownDomainObjectException
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.enterprise.core.GradleEnterprisePluginManager
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

class GradleScanLinkListener(override var scanLink: String? = null) : GradleScanLinkAccumulator {
    override fun execute(publishedBuildScan: PublishedBuildScan) {
        scanLink = publishedBuildScan.buildScanUri.toString()
    }
}

interface GradleScanLinkAccumulator : Action<PublishedBuildScan> {
    val scanLink: String?
}