package io.github.cdsap.talaiot.configuration

import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.Project
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
    }
})
