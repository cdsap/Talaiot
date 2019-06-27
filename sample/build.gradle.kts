import com.cdsap.talaiot.logger.LogTracker

repositories {
    jcenter()
    mavenCentral()
    google()
    mavenLocal()
}

plugins {
    kotlin("jvm") version "1.3.11"
    id("talaiot") version "0.4.0"
}

talaiot {
    logger = LogTracker.Mode.INFO
    publishers {
        outputPublisher
        taskDependencyGraphPublisher {
            html = true
        }
    }
}