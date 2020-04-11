package com.cdsap.talaiot.mock

import com.cdsap.talaiot.metrics.SimpleMetric
import com.cdsap.talaiot.metrics.base.CmdMetric

class AdbVersionMetric : CmdMetric(
    cmd = "adb version",
    assigner = { report, value ->
        report.customProperties.buildProperties["adbVersion"] = value
    }
)

class KotlinVersionMetric : SimpleMetric<String>(
    provider = { "1.4" },
    assigner = { report, value ->
        report.customProperties.buildProperties["kotlinVersion"] = value
    }
)
