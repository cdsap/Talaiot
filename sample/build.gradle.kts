import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.Publisher
import com.cdsap.talaiot.entities.ExecutionReport

repositories {
    mavenLocal()
    jcenter()
    mavenCentral()
    google()

}

plugins {
    kotlin("jvm") version "1.3.60"
    id("talaiot") version "1.0.12-SNAPSHOT"
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
            dbName = "tracking2"
            url = "http://localhost:8086"
            taskMetricName = "task"
            buildMetricName = "build"
            publishTaskMetrics = false
        }
        customPublisher = CustomPublisher()
    }
}

class CustomPublisher : Publisher {

    override fun publish(report: ExecutionReport) {
        println("[CustomPublisher] : Number of tasks = ${report.tasks?.size}")
    }
}