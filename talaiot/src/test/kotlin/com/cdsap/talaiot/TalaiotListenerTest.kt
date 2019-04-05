package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.publisher.TalaiotPublisher
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.internal.tasks.TaskStateInternal
import org.gradle.api.invocation.Gradle
import org.gradle.kotlin.dsl.extra
import org.gradle.plugins.ide.internal.tooling.model.LaunchableGradleTask
import org.gradle.testfixtures.ProjectBuilder


class TalaiotListenerTest : BehaviorSpec({

    given("a TalaiotListener") {
        `when`("build is executed by assemble and clean tasks") {
            val project = ProjectBuilder.builder().build()
            val result: BuildResult = mock()
            val talaiotListener = initListener(arrayOf("clean", "assemble"), project)
            provideTasks(arrayOf("clean", "myTask", "assemble"), project, talaiotListener)
            talaiotListener.buildFinished(result)

            then(" assemble is considered as rootNode and clean doesn't") {

                verify(talaiotListener.talaiotPublisher).publish(argThat {
                    this.size == 3
                            && this[0].taskName == "clean"
                            && this[0].state == TaskMessageState.EXECUTED
                            && !this[0].rootNode
                            && this[1].taskName == "myTask"
                            && this[1].state == TaskMessageState.EXECUTED
                            && !this[1].rootNode
                            && this[2].taskName == "assemble"
                            && this[2].state == TaskMessageState.EXECUTED
                            && this[2].rootNode

                })
            }
        }
        `when`("build is finished but ignored ") {
            val project = ProjectBuilder.builder().build()
            val result: BuildResult = mock()
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.ignoreWhen {
                envName = "CI"
                envValue = "true"
            }
            project.extra.set("CI", "true")

            val talaiotListener = initListener(arrayOf("clean", "assemble"), project, talaiotExtension)
            provideTasks(arrayOf("clean", "myTask", "assemble"), project, talaiotListener)
            talaiotListener.buildFinished(result)


            talaiotListener.buildFinished(result)
            then("Publisher doesn't publish results with the tasks processed") {
                verify(talaiotListener.talaiotPublisher, never()).publish(any())
            }
        }
    }
})


fun initListener(
    elements: Array<String>,
    project: Project,
    talaiotExtension: TalaiotExtension? = null
): TalaiotListener {
    val talaiotPublisher: TalaiotPublisher = mock()
    val talaiotListener = TalaiotListener(talaiotPublisher, talaiotExtension ?: TalaiotExtension(project))
    val gradle: Gradle = mock()
    val startParameter: StartParameter = mock()

    val taskRequest = mutableListOf<LaunchableGradleTask>()
    elements.forEach {
        taskRequest.add(LaunchableGradleTask().setPath(it))
    }
    whenever(startParameter.taskRequests).thenReturn(taskRequest.toList())
    whenever(gradle.startParameter).thenReturn(startParameter)
    talaiotListener.projectsEvaluated(gradle)
    return talaiotListener

}

fun provideTasks(tasks: Array<String>, project: Project, talaiotListener: TalaiotListener) {
    tasks.forEach {
        val task = project.task(it)
        talaiotListener.beforeExecute(task)
        talaiotListener.afterExecute(task, TaskStateInternal())
    }

}