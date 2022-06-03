package io.github.cdsap.talaiot.entities

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class ExecutionReportTest : BehaviorSpec({
    given("An execution report and a list of tasks") {
        val report = ExecutionReport()
        val allTasks = listOf(
            TaskLength(
                ms = 1000L,
                taskName = "task1",
                taskPath = "module:task1",
                state = TaskMessageState.EXECUTED,
                module = "module"
            ),
            TaskLength(
                ms = 1000L,
                taskName = "task2",
                taskPath = "module:task2",
                state = TaskMessageState.FROM_CACHE,
                module = "module"
            )
        )

        `when`("all tasks are present") {
            report.tasks = allTasks
            report.unfilteredTasks = allTasks

            then("cache ratio is 0.5") {
                report.cacheRatio.shouldBe("0.5")
            }
        }

        `when`("all tasks are filtered out") {
            report.tasks = emptyList()
            report.unfilteredTasks = allTasks

            then("cache ratio is 0.5") {
                report.cacheRatio.shouldBe("0.5")
            }
        }
    }
})
