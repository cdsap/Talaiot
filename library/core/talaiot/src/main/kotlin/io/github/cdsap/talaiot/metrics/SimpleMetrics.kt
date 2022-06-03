package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.metrics.base.GradleMetric
import io.github.cdsap.talaiot.metrics.base.Metric
import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.scan.scopeids.BuildScanScopeIds
import java.net.InetAddress
import java.net.UnknownHostException
import java.nio.charset.Charset
import java.util.UUID

open class SimpleMetric<T>(provider: (Unit) -> T, assigner: (ExecutionReport, T) -> Unit) :
    Metric<T, Unit>(provider, assigner)

class UserMetric : GradleMetric<String>(
    provider = { project: Project -> project.providers.systemProperty("user.name").forUseAtConfigurationTime().get() },
    assigner = { report, value -> report.environment.username = value }
)

class OsMetric :
    SimpleMetric<String>(
        provider = { "${OperatingSystem.current().name}-${OperatingSystem.current().version}" },
        assigner = { report, value -> report.environment.osVersion = value }
    )

class BuildIdMetric : SimpleMetric<String>(
    provider = { UUID.randomUUID().toString() },
    assigner = { report, value -> report.buildId = value }
)

/**
 * As described in the source of gradle:
 * The ID of a single build invocation.
 *
 * Here, the term "build" is used to represent the overall invocation.
 * For example, buildSrc shares the same build scope ID as the overall build.
 * All composite participants also share the same build scope ID.
 * That is, all “nested” builds (in terms of GradleLauncher etc.) share the same build ID.
 *
 * This ID is, by definition, not persistent.
 */
class GradleBuildInvocationIdMetric : GradleMetric<String>(
    provider = { project: Project -> (project.gradle as GradleInternal).services[BuildScanScopeIds::class.java].buildInvocationId },
    assigner = { report, value -> report.buildInvocationId = value }
)

class ProcessorCountMetric : SimpleMetric<String>(
    provider = { Runtime.getRuntime().availableProcessors().toString() },
    assigner = { report, value -> report.environment.cpuCount = value }
)

class JavaVmNameMetric : SimpleMetric<String>(
    provider = { System.getProperty("java.runtime.version") },
    assigner = { report, value -> report.environment.javaVmName = value }
)

class LocaleMetric : GradleMetric<String>(
    provider = { project: Project ->
        project.providers.systemProperty("user.language").forUseAtConfigurationTime().get()
    },
    assigner = { report, value -> report.environment.locale = value }
)

class HostnameMetric : SimpleMetric<String>(
    provider = {
        try {
            InetAddress.getLocalHost().hostName
        } catch (e: UnknownHostException) {
            // Issue https://github.com/cdsap/Talaiot/issues/314
            // InetAddress.getLocalHost() ignores the /etc/resolv.conf, but only looks at the /etc/hosts file
            // https://stackoverflow.com/a/1881967
            ""
        }
    },
    assigner = { report, value -> report.environment.hostname = value }
)

class DefaultCharsetMetric : SimpleMetric<String>(
    provider = { Charset.defaultCharset().toString() },
    assigner = { report, value -> report.environment.defaultChartset = value }
)
