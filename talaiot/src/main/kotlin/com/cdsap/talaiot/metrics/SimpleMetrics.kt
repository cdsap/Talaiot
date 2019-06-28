package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.metrics.base.Metric
import com.cdsap.talaiot.entities.ExecutionReport
import org.gradle.internal.os.OperatingSystem
import oshi.SystemInfo
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
        assigner = { report, value -> report.environment.osVersion = value}
    )

class BuildIdMetric : SimpleMetric<String>(
    provider = { UUID.randomUUID().toString() },
    assigner = { report, value -> report.buildId = value }
)

class ProcessorCountMetric : SimpleMetric<String>(
    provider = { Runtime.getRuntime().availableProcessors().toString() },
    assigner = { report, value -> report.environment.cpuCount = value }
)

class RamAvailableMetric : SimpleMetric<String>(
    provider = { SystemInfo().hardware.memory.available.toString() },
    assigner = { report, value -> report.environment.totalRamAvailableBytes = value }
)

class JavaRuntimeMetric : SimpleMetric<String>(
    provider = { Runtime.getRuntime().toString() },
    assigner = { report, value -> report.environment.javaRuntime = value }
)

class JavaVmNameMetric : SimpleMetric<String>(
    provider = { System.getProperty("java.runtime.version") },
    assigner = { report, value -> report.environment.javaVmName = value}
)

class LocaleMetric : SimpleMetric<String>(
    provider = { System.getProperty("user.language") },
    assigner = { report, value -> report.environment.locale = value }
)

class PublicIpMetric : SimpleMetric<String>(
    provider = { TODO("implement") },
    assigner = { report, value -> report.environment.publicIp = value }
)

class DefaultCharsetMetric : SimpleMetric<String>(
    provider = { TODO("implement") },
    assigner = { report, value -> report.environment.defaultChartset = value }
)

class IDEVersionCharsetMetric : SimpleMetric<String>(
    provider = { TODO("implement") },
    assigner = { report, value -> report.environment.ideVersion = value }
)