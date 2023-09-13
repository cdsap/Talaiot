package io.github.cdsap.talaiot.util

import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class TaskAbbreviationMatcherTest : BehaviorSpec({
    given("a TaskAbbreviationMatcher") {
        val executedTasks = listOf(
            TaskName("assembleDebug", ":app:assembleDebug"),
            TaskName("compileKotlin", ":app:compileKotlin"),
            TaskName("assembleDebug", ":moduleA:assembleDebug"),
            TaskName("compileKotlin", ":moduleA:compileKotlin"),
            TaskName("assembleDebug", ":modules:moduleB:assembleDebug"),
            TaskName("compileKotlin", ":modules:moduleB:compileKotlin"),
            TaskName("assembleDebug", "assembleDebug")
        )
        val taskAbbreviationMatcher = TaskAbbreviationMatcher(executedTasks)

        `when`("search abbreviation for single task") {
            then("return full task name") {
                val fullTaskName = taskAbbreviationMatcher.findRequestedTask(":app:assDeb")
                fullTaskName.shouldBe(":app:assembleDebug")
            }
        }

        `when`("search abbreviation for single task without leading semicolon") {
            then("return full task name") {
                val fullTaskName = taskAbbreviationMatcher.findRequestedTask("app:assDeb")
                fullTaskName.shouldBe(":app:assembleDebug")
            }
        }

        `when`("search abbreviation for top task") {
            then("return abbreviation") {
                val fullTaskName = taskAbbreviationMatcher.findRequestedTask("aD")
                fullTaskName.shouldBe("assembleDebug")
            }
        }

        `when`("search abbreviation for a submovule task") {
            then("return abbreviation") {
                val fullTaskName = taskAbbreviationMatcher.findRequestedTask(":modules:moduleB:aD")
                fullTaskName.shouldBe(":modules:moduleB:assembleDebug")
            }
        }

        `when`("search abbreviation for non existent task") {
            then("return abbreviation") {
                val fullTaskName = taskAbbreviationMatcher.findRequestedTask("moduleB:assDeb")
                fullTaskName.shouldBe("moduleB:assDeb")
            }
        }
    }
})
