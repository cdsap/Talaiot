package com.cdsap.talaiot.filter

import com.cdsap.talaiot.configuration.BuildFilterConfiguration
import com.cdsap.talaiot.entities.ExecutionReport
import com.cdsap.talaiot.utils.TestLogTrackerRecorder
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.specs.BehaviorSpec

class BuildFilterProcessorTest: BehaviorSpec({
    given("A logger") {
        val logTracker = TestLogTrackerRecorder

        `when`("Configuration is not specified") {
            val configuration = BuildFilterConfiguration().apply {

            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("failed build is published"){
                val report = ExecutionReport(success = false)
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }

            then("successful build is published"){
                val report = ExecutionReport(success = true, requestedTasks = "app:taskA")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }

        `when`("configure for only successful builds") {
            val configuration = BuildFilterConfiguration().apply {
                success = true
            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("failed build is not published"){
                val report = ExecutionReport(success = false)
                buildFilterProcessor.shouldPublishBuild(report).shouldBeFalse()
            }

            then("successful build is published"){
                val report = ExecutionReport(success = true, requestedTasks = "app:taskA")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }

        `when`("configure to exclude tasks") {
            val configuration = BuildFilterConfiguration().apply {
                requestedTasks.excludes = arrayOf(":app:taskA", ":app:taskB")
            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("do not publish if all requested tasks excluded"){
                val report = ExecutionReport(success = false, requestedTasks = ":app:taskA :app:taskB")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeFalse()
            }

            then("publish if at least one requested task is not filtered out"){
                val report = ExecutionReport(success = true, requestedTasks = ":app:taskC")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }

        `when`("configure to exclude tasks via regex") {
            val configuration = BuildFilterConfiguration().apply {
                requestedTasks.excludes = arrayOf(":app:task.*")
            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("do not publish if all requested tasks excluded"){
                val report = ExecutionReport(success = false, requestedTasks = ":app:taskA :app:taskB")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeFalse()
            }

            then("publish if at least one requested task is not filtered out"){
                val report = ExecutionReport(success = true, requestedTasks = ":module:taskC")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }

        `when`("configure to include and tasks") {
            val configuration = BuildFilterConfiguration().apply {
                requestedTasks.includes = arrayOf(":app:assembleDebug")
            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("do not publish if requested task doesn't match included"){
                val report = ExecutionReport(success = false, requestedTasks = ":app:taskA :app:taskB")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeFalse()
            }

            then("publish if at least one requested task is not filtered out"){
                val report = ExecutionReport(success = true, requestedTasks = ":app:assembleDebug")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }

        `when`("configure to include and exclude tasks") {
            val configuration = BuildFilterConfiguration().apply {
                requestedTasks.excludes = arrayOf(":app:task*")
                requestedTasks.includes = arrayOf(":app:taskA")
            }
            val buildFilterProcessor = BuildFilterProcessor(logTracker, configuration)

            then("include has priority and report is published "){
                val report = ExecutionReport(success = false, requestedTasks = ":app:taskA :app:taskB")
                buildFilterProcessor.shouldPublishBuild(report).shouldBeTrue()
            }
        }
    }
})
