import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.metrics.HostnameMetric
import com.cdsap.talaiot.metrics.SimpleMetric
import com.cdsap.talaiot.publisher.Publisher

repositories {
    jcenter()
    mavenCentral()
    google()
    mavenLocal()
}

plugins {
    kotlin("jvm") version "1.3.60"
    id("com.cdsap.talaiot") version "1.2.1-SNAPSHOT"
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.3.60")
}

talaiot {
    logger = LogTracker.Mode.INFO
    publishers {
        timelinePublisher = true
        taskDependencyGraphPublisher {
            html = true
            gexf = true
        }
        influxDbPublisher {
            dbName = "tracking"
            url = "http://localhost:8086"
            taskMetricName = "task"
            buildMetricName = "build"
        }
        customPublisher = CustomPublisher()
    }

    metrics {
        // Talaiot provides a few methods to disable a group of metrics at once
        // By default all groups are enabled
        performanceMetrics = false
        gradleSwitchesMetrics = false
        environmentMetrics = false

        // You can also add your own custom Metric objects:
        customMetrics(
            HelloMetric(),
            // Including some of the provided metrics, individually.
            HostnameMetric()
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

class CustomPublisher : Publisher {
    override fun publish(report: ExecutionReport) {
        println("[CustomPublisher] : Number of tasks = ${report.tasks?.size}")
        println("[CustomPublisher] : HelloMetric = ${report.customProperties.buildProperties["hello"]}")
        println("[CustomPublisher] : Kotlin = ${report.customProperties.buildProperties["kotlin"]}")
        println("[CustomPublisher] : Java = ${report.customProperties.buildProperties["java"]}")
        println("[CustomPublisher] : PID = ${report.customProperties.taskProperties["pid"]}")
    }
}

class HelloMetric : SimpleMetric<String>(
    provider = { "Hello!" },
    assigner = { report, value -> report.customProperties.buildProperties["hello"] = value }
)
