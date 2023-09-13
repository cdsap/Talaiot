buildscript {
    repositories {
        mavenCentral()
        maven(url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
    dependencies {
        classpath("io.github.cdsap:talaiot:2.0.0-SNAPSHOT")
    }
}
apply(plugin = "io.github.cdsap.talaiot")
plugins {
    `java`
}


configure<io.github.cdsap.talaiot.plugin.TalaiotPluginExtension> {
    logger = io.github.cdsap.talaiot.logger.LogTracker.Mode.INFO
    publishers {

        jsonPublisher = true
        influxDbPublisher {
            dbName = "tracking"
            url = "http://localhost:8086"
            taskMetricName = "task"
            buildMetricName = "build"
        }

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