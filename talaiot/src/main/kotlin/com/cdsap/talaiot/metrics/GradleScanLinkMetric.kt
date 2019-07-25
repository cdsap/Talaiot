package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.metrics.base.BuildResultMetric
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.logging.LoggingManagerInternal
import org.gradle.internal.logging.events.OutputEvent
import org.gradle.internal.logging.events.StyledTextOutputEvent
import org.gradle.internal.scan.eob.DefaultBuildScanEndOfBuildNotifier
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
            GradleVersion.current() > GradleVersion.version("5.0") -> {
                services.get(
                    DefaultBuildScanEndOfBuildNotifier::class.java
                ) as DefaultBuildScanEndOfBuildNotifier
            }
            else -> {
                /**
                 * Previously the code registered a regular buildListener
                 * This is unsupported currently
                 */

                null
            }
        }?.let { endOfBuildNotifier ->
            val loggingManager = gradleInternal.services.get(LoggingManagerInternal::class.java)

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

            endOfBuildNotifier.fireBuildComplete(result.failure)

            val listenerField = endOfBuildNotifier::class.java.getDeclaredField("listener")
            listenerField.isAccessible = true
            listenerField.set(endOfBuildNotifier, null)

            loggingManager.removeOutputEventListener(listener)

            link
        }
    },
    assigner = { report, value ->
        report.scanLink = value
    }
)