package io.github.cdsap.talaiot.filter

import io.github.cdsap.talaiot.configuration.BuildFilterConfiguration
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.logger.LogTracker

class BuildFilterProcessor(
    val logTracker: LogTracker,
    val filter: BuildFilterConfiguration
) : java.io.Serializable {

    fun shouldPublishBuild(report: ExecutionReport): Boolean {
        val successAllowsPublishing = report.success == filter.success || filter.success == null
        return if (successAllowsPublishing) {
            val processor = StringFilterProcessor(filter.requestedTasks, logTracker)
            return report.requestedTasks?.split(" ")?.any {
                processor.matches(it)
            } ?: true
        } else {
            false
        }
    }
}
