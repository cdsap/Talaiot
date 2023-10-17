package io.github.cdsap.talaiot.publisher.pushgateway

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.prometheus.client.CollectorRegistry

class PushGatewayTaskCollector(
    val report: ExecutionReport,
    private val registry: CollectorRegistry,
    private val pushGatewayLabelProvider: PushGatewayLabelProvider
) : PushGatewayCollector {

    override fun collect() {
        report.tasks?.forEach {
            val labelValuesTask = pushGatewayLabelProvider.taskLabelValues(it).toTypedArray()
            val labelNamesTask = pushGatewayLabelProvider.taskLabelNames().toTypedArray()

            gaugeBuild(
                "gradle_task_${it.taskName}",
                "Gradle task ${it.taskName}",
                (it.ms).toDouble(),
                registry,
                labelNamesTask,
                labelValuesTask
            )
        }
    }
}
