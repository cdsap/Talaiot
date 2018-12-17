package com.cdsap.talaiot.ci

import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.internal.impldep.junit.framework.Assert.assertFalse
import org.gradle.internal.impldep.junit.framework.Assert.assertTrue
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class IgnoreWhenTest : BehaviorSpec({
    given("IgnoreWhen configuration") {
        val project: Project = ProjectBuilder().build()

        `when`("There is no property ") {
            val ignoreWhen = IgnoreWhen(project)
            then("I shouldn't ignore the execution") {
                assertFalse(ignoreWhen.shouldIgnore())
            }
        }
        `when`("There isn't  matching property  ") {
            project.extra.set("CI", "false")
            val ignoreWhen = IgnoreWhen(project)
            ignoreWhen.envName = "CI"
            ignoreWhen.envValue = "true"
            then("I shouldn;t ignore the execution") {
                assertFalse(ignoreWhen.shouldIgnore())
            }
        }
        `when`("There is matching property ") {
            project.extra.set("CI", "true")
            val ignoreWhen = IgnoreWhen(project)
            ignoreWhen.envName = "CI"
            ignoreWhen.envValue = "true"
            then("I shouldn;t ignore the execution") {
                assertTrue(ignoreWhen.shouldIgnore())
            }
        }
    }
})