package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.TalaiotExtension
import com.nhaarman.mockitokotlin2.mock
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project


class CustomMetricsTest : BehaviorSpec({
    given("CustomMetrics") {
        val project: Project = mock()
        `when`("There are no custom metrics defined in the extension") {
            val customMetrics = CustomMetrics(TalaiotExtension((project)))
            then("it returns empty Map") {
                assert(customMetrics.get().isEmpty())
            }
        }
        `when`("there are custom metrics defined") {
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.metrics.customMetrics(Pair("gradleVersion", "5.1"))
            val customMetrics = CustomMetrics(talaiotExtension)
            then("it returns the metrics defined in the extension") {
                assert(customMetrics.get().containsKey("gradleVersion"))
                assert(customMetrics.get().containsValue("5.1"))
            }
        }
    }
})