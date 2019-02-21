package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.publisher.TalaiotPublisher
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.StartParameter
import org.gradle.api.internal.tasks.TaskStateInternal
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.extra
import org.gradle.plugins.ide.internal.tooling.model.LaunchableGradleTask
import org.gradle.testfixtures.ProjectBuilder


class TalaiotListenerTest : BehaviorSpec({

    given("a TalaiotListener") {
        val project = ProjectBuilder.builder().build()
        val result: BuildResult = mock()
        `when`("build is finished") {
            val talaiotPublisher: TalaiotPublisher = mock()
            val talaiotExtension = TalaiotExtension(project)
            val talaiotListener = TalaiotListener(talaiotPublisher, talaiotExtension)
            val startParameter: StartParameter = mock()
            val gradle: Gradle = mock()
            val launchableGradleTaskClean = LaunchableGradleTask().setPath("clean")
            val launchableGradleTaskAssemble = LaunchableGradleTask().setPath("assemble")


            val taskRequest = listOf(launchableGradleTaskClean, launchableGradleTaskAssemble)

            whenever(startParameter.taskRequests).thenReturn(taskRequest)
            whenever(gradle.startParameter).thenReturn(startParameter)
            talaiotListener.projectsEvaluated(gradle)

            val taskClean = project.task("clean")
            talaiotListener.beforeExecute(taskClean)
            talaiotListener.afterExecute(taskClean, TaskStateInternal())
            val taskMyTask = project.task("myTask")
            talaiotListener.beforeExecute(taskMyTask)
            talaiotListener.afterExecute(taskMyTask, TaskStateInternal())
            val taskAssemble = project.task("assemble")
            talaiotListener.beforeExecute(taskAssemble)
            talaiotListener.afterExecute(taskAssemble, TaskStateInternal())
            talaiotListener.buildFinished(result)

            then("Publisher publish results with the tasks processed") {

                verify(talaiotPublisher).publish(argThat {
                    this.size == 3
                            && this[0].taskName == "clean"
                            && this[0].state == TaskMessageState.EXECUTED
                            && this[1].taskName == "myTask"
                            && this[1].state == TaskMessageState.EXECUTED
                            && this[2].taskName == "assemble"
                            && this[2].state == TaskMessageState.EXECUTED

                })
            }
        }

        `when`("build is finished and ignore ") {
            val talaiotPublisher: TalaiotPublisher = mock()
            val talaiotExtension = TalaiotExtension(project)
            val startParameter: StartParameter = mock()
            val launchableGradleTask = LaunchableGradleTask()
            launchableGradleTask.path = "clean, assemble"
            val taskRequest = listOf(launchableGradleTask)
            val gradle: Gradle = mock()
            whenever(startParameter.taskRequests).thenReturn(taskRequest)
            whenever(gradle.startParameter).thenReturn(startParameter)

            talaiotExtension.ignoreWhen {
                envName = "CI"
                envValue = "true"
            }
            project.extra.set("CI", "true")
            val talaiotListener = TalaiotListener(talaiotPublisher, talaiotExtension)
            val task = project.task("myTask2")
            talaiotListener.projectsEvaluated(gradle)
            talaiotListener.beforeExecute(task)
            talaiotListener.afterExecute(task, TaskStateInternal())

            talaiotListener.buildFinished(result)
            then("Publisher doesn't publish results with the tasks processed") {
                verify(talaiotPublisher, never()).publish(any())
            }
        }
    }
})
