//import com.cdsap.talaiot.entities.TaskMeasurementAggregated
//import com.cdsap.talaiot.logger.LogTracking
//import com.cdsap.talaiot.publisher.Publisher

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("talaiot")
}


android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.example.ivillar.myapplication"
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
    }
}

talaiot {

//    publishers {
//        outputPublisher
//
//        influxDbPublisher {
//            dbName = "tracking"
//            url = "http://localhost:8086"
//            urlMetric = "tracking"
//        }
//    }
//    ignoreWhen {
//        envName = "CI"
//        envValue = "false"
//    }
//
//    metrics {
//        gitMetrics = true
//        performanceMetrics = true
//        customMetrics("gradleHomeDir" to project.gradle.gradleHomeDir.toString())
//    }

}


//class CustomPublisher : Publisher {
//    override var logTracker = LogTracking(LogTracking.Mode.INFO)
//
//    override fun publish(measurementAggregated: TaskMeasurementAggregated) {
//        logTracker.log("================")
//        logTracker.log("CustomPublisher")
//        logTracker.log("================")
//        val a = measurementAggregated.toString()
//        logTracker.log(a)
//
//
//    }
//}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.2.71")
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    testImplementation("junit:junit:4.12")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
    androidTestImplementation("com.android.support.test.espresso:espresso-core:3.0.2")
}
