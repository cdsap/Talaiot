package com.cdsap.talaiot.configuration

import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class IgnoreWhenConfigurationTest : BehaviorSpec({
    given("IgnoreWhenConfiguration configuration") {
        val project: Project = ProjectBuilder().build()

        `when`("There is no property ") {
            val ignoreWhen = IgnoreWhenConfiguration(project)
            then("I should not ignore the execution") {
                assert(!ignoreWhen.shouldIgnore())
            }
        }
        `when`("There isn't  matching property  ") {
            project.gradle.rootProject.extra.set("CI", "false")
            project.extra.set("CI", "false")
            val ignoreWhenConfiguration = IgnoreWhenConfiguration(project)
            ignoreWhenConfiguration.envName = "CI"
            ignoreWhenConfiguration.envValue = "true"
            then("I should not ignore the execution") {
                assert(!ignoreWhenConfiguration.shouldIgnore())
            }
        }
        `when`("There is matching property ") {
            project.gradle.rootProject.extra.set("CI", "true")
            val ignoreWhen = IgnoreWhenConfiguration(project)
            ignoreWhen.envName = "CI"
            ignoreWhen.envValue = "true"
            then("I should ignore the execution") {
                assert(ignoreWhen.shouldIgnore())
            }
        }
    }
})