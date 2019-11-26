import com.cdsap.talaiot.logger.LogTracker

repositories {
    jcenter()
    mavenCentral()
    google()
    mavenLocal()
}

plugins {
    kotlin("jvm") version "1.3.11"
    id("talaiot") version "1.0.10-SNAPSHOT"
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
    }
}