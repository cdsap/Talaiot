plugins {
    `java`
    id("io.github.cdsap.talaiot") version "2.0.5-SNAPSHOT"
}


talaiot {
    logger = io.github.cdsap.talaiot.logger.LogTracker.Mode.INFO
    publishers {

        jsonPublisher = true
        influxDbPublisher {
            dbName = "tracking"
            url = "http://localhost:8086"
            taskMetricName = "task"
            buildMetricName = "build"
        }

        customPublishers(CustomPublisher())

    }

    metrics {
        // Talaiot provides a few methods to disable a group of metrics at once.
        // By default all groups are enabled.
        performanceMetrics = false
        gradleSwitchesMetrics = false
        environmentMetrics = false

        // You can also add your own custom Metric objects:
        customMetrics(
            HelloMetric(),
            // Including some of the provided metrics, individually.
            io.github.cdsap.talaiot.metrics.HostnameMetric()
        )

        // Or define build or task metrics directly:
        customBuildMetrics(
            "kotlin" to "1.4",
            "java" to "8"
        )
        customTaskMetrics(
            "pid" to "2134"
        )
    }
}

class HelloMetric : io.github.cdsap.talaiot.metrics.SimpleMetric<String>(
    provider = { "Hello!" },
    assigner = { report, value -> report.customProperties.buildProperties["hello"] = value }
)

class CustomPublisher : io.github.cdsap.talaiot.publisher.Publisher {

    override fun publish(report: io.github.cdsap.talaiot.entities.ExecutionReport) {
        println("[CustomPublisher] : Number of tasks = ${report.tasks?.size}")
        println("[CustomPublisher] : Kotlin = ${report.customProperties.buildProperties["kotlin"]}")
        println("[CustomPublisher] : Java = ${report.customProperties.buildProperties["java"]}")
        println("[CustomPublisher] : PID = ${report.customProperties.taskProperties["pid"]}")
    }
}
