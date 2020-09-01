package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.metrics.base.GradleMetric
import com.cdsap.talaiot.metrics.base.Metric
import org.gradle.api.Project
import org.gradle.api.internal.GradleInternal
import org.gradle.internal.os.OperatingSystem
import org.gradle.internal.scan.scopeids.BuildScanScopeIds
import oshi.SystemInfo
import java.net.InetAddress
import java.nio.charset.Charset
import java.util.*

open class SimpleMetric<T>(provider: (Unit) -> T, assigner: (ExecutionReport, T) -> Unit) :
    Metric<T, Unit>(provider, assigner)

class UserMetric : SimpleMetric<String>(
    provider = { System.getProperty("user.name") },
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

class RamAvailableMetric : SimpleMetric<String>(
    provider = { SystemInfo().hardware.memory.available.toString() },
    assigner = { report, value -> report.environment.totalRamAvailableBytes = value }
)

class JavaVmNameMetric : SimpleMetric<String>(
    provider = { System.getProperty("java.runtime.version") },
    assigner = { report, value -> report.environment.javaVmName = value }
)

class LocaleMetric : SimpleMetric<String>(
    provider = { System.getProperty("user.language") },
    assigner = { report, value -> report.environment.locale = value }
)

class OsManufacturerMetric : SimpleMetric<String>(
    provider = { SystemInfo().operatingSystem.manufacturer },
    assigner = { report, value -> report.environment.osManufacturer = value }
)

class HostnameMetric : SimpleMetric<String>(
    provider = { InetAddress.getLocalHost().hostName },
    assigner = { report, value -> report.environment.hostname = value }
)

class PublicIpMetric : SimpleMetric<String?>(
    provider = {
        /**
         * For simplicity we get the first available ip of the first network device
         */
        SystemInfo().hardware.networkIFs.firstOrNull()?.let { nif ->
            nif.iPv4addr.firstOrNull()
        } ?: null
    },
    assigner = { report, value -> report.environment.publicIp = value }
)

class DefaultCharsetMetric : SimpleMetric<String>(
    provider = { Charset.defaultCharset().toString() },
    assigner = { report, value -> report.environment.defaultChartset = value }
)