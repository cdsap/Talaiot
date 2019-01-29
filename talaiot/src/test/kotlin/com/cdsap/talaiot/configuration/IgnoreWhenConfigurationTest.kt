package com.cdsap.talaiot.configuration

import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class IgnoreWhenConfigurationTest : BehaviorSpec({
    given("IgnoreWhenConfiguration configuration") {
        val project: Project = ProjectBuilder().build()

        `when`("There is no property") {
            val ignoreWhen = IgnoreWhenConfiguration(project)
            then("It should not ignore the execution") {
                assert(!ignoreWhen.shouldIgnore())
            }
        }
        `when`("There isn't a matching property  ") {
            project.extra.set("CI", "false")
            val ignoreWhenConfiguration = IgnoreWhenConfiguration(project)
            ignoreWhenConfiguration.envName = "CI"
            ignoreWhenConfiguration.envValue = "true"
            then("It should not ignore the execution") {
                assert(!ignoreWhenConfiguration.shouldIgnore())
            }
        }
        `when`("There is a matching property ") {
            project.gradle.rootProject.extra.set("CI", "true")
            val ignoreWhen = IgnoreWhenConfiguration(project)
            ignoreWhen.envName = "CI"
            ignoreWhen.envValue = "true"
            then("It should ignore the execution") {
                assert(ignoreWhen.shouldIgnore())
            }
        }
    }
})