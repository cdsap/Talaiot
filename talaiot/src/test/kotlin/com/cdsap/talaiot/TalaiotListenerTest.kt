package com.cdsap.talaiot

import com.cdsap.talaiot.entities.TaskMessageState
import com.cdsap.talaiot.publisher.TalaiotPublisher
import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import io.kotlintest.specs.BehaviorSpec
import org.gradle.BuildResult
import org.gradle.api.internal.tasks.TaskStateInternal
import org.gradle.testfixtures.ProjectBuilder


class TalaiotListenerTest : BehaviorSpec({
    given("a TalaiotListener") {
        val project = ProjectBuilder.builder().build()
        val result: BuildResult = mock()
        val talaiotPublisher: TalaiotPublisher = mock()
        val talaiotListener = TalaiotListener(talaiotPublisher)
        val task = project.task("myTask")
        talaiotListener.beforeExecute(task)
        talaiotListener.afterExecute(task, TaskStateInternal())
        `when`("build is finished") {
            talaiotListener.buildFinished(result)
            then("Publisher publish results with the tasks processed") {

                verify(talaiotPublisher).publish(argThat {
                    this.size == 1
                            && this[0].taskName == ":myTask"
                            && this[0].state == TaskMessageState.NO_MESSAGE_STATE

                })
            }
        }
    }
})
