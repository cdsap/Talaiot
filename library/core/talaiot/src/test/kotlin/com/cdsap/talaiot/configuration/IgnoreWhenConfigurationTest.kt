package com.cdsap.talaiot.configuration

import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class IgnoreWhenConfigurationTest : BehaviorSpec({
    given("IgnoreWhenConfiguration configuration") {

        `when`("There is no property") {
            val project: Project = ProjectBuilder.builder().build()
            val ignoreWhen = IgnoreWhenConfiguration(project)
            then("It should not ignore the execution") {
                assert(!ignoreWhen.shouldIgnore())
            }
        }
        `when`("There isn't a matching property  ") {
            val project: Project = ProjectBuilder.builder().build()
            project.extra.set("execute", "false")
            val ignoreWhenConfiguration = IgnoreWhenConfiguration(project)
            ignoreWhenConfiguration.envName = "execute"
            ignoreWhenConfiguration.envValue = "true"
            then("It should not ignore the execution tw") {
                assert(!ignoreWhenConfiguration.shouldIgnore())
            }
        }
        `when`("There is a matching property ") {
            val project: Project = ProjectBuilder.builder().build()
            project.gradle.rootProject.extra.set("execute", "true")
            val ignoreWhen = IgnoreWhenConfiguration(project)
            ignoreWhen.envName = "execute"
            ignoreWhen.envValue = "true"
            then("It should ignore the execution") {
                assert(ignoreWhen.shouldIgnore())
            }
        }
    }
})