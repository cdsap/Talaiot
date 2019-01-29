package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.publisher.TalaiotPublisher
import com.nhaarman.mockitokotlin2.*
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.api.internal.tasks.TaskStateInternal
import org.gradle.kotlin.dsl.extra
import org.gradle.testfixtures.ProjectBuilder


class TalaiotListenerTest : BehaviorSpec({
    given("a TalaiotListener") {
        val project = ProjectBuilder.builder().build()
        val result: BuildResult = mock()
        `when`("build is finished") {
            val talaiotPublisher: TalaiotPublisher = mock()
            val talaiotExtension = TalaiotExtension(project)
            val talaiotListener = TalaiotListener(talaiotPublisher, talaiotExtension)
            val task = project.task("myTask")
            talaiotListener.beforeExecute(task)
            talaiotListener.afterExecute(task, TaskStateInternal())
            talaiotListener.buildFinished(result)
            then("Publisher publish results with the tasks processed") {

                verify(talaiotPublisher).publish(argThat {
                    this.size == 1
                            && this[0].taskName == ":myTask"
                            && this[0].state == TaskMessageState.NO_MESSAGE_STATE

                })
            }
        }

        `when`("build is finished and ignore ") {
            val talaiotPublisher: TalaiotPublisher = mock()
            val talaiotExtension = TalaiotExtension(project)
            talaiotExtension.ignoreWhen {
                envName = "CI"
                envValue = "true"
            }
            project.extra.set("CI", "true")
            val talaiotListener = TalaiotListener(talaiotPublisher, talaiotExtension)
            val task = project.task("myTask2")
            talaiotListener.beforeExecute(task)
            talaiotListener.afterExecute(task, TaskStateInternal())

            talaiotListener.buildFinished(result)
            then("Publisher doesn't publish results with the tasks processed") {
                verify(talaiotPublisher, never()).publish(any())
            }
        }
    }
})
