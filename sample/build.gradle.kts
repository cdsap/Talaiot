import com.cdsap.talaiot.logger.LogTracker
import com.cdsap.talaiot.publisher.timeline.TimelinePublisher

repositories {
    jcenter()
    mavenCentral()
    google()
    mavenLocal()
}

plugins {
    kotlin("jvm") version "1.3.11"
    id("talaiot") version "0.4.0-SNAPSHOT"
}

talaiot {
    logger = LogTracker.Mode.INFO
    publishers {
        customPublisher = TimelinePublisher(gradle)
        taskDependencyGraphPublisher {
            html = true
        }
    }
}