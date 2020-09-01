package com.cdsap.talaiot

import com.cdsap.talaiot.entities.CacheInfo
import com.cdsap.talaiot.entities.ExecutedGradleTaskInfo
import com.cdsap.talaiot.entities.ExecutedTasksInfo
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.internal.tasks.execution.ExecuteTaskBuildOperationType
import org.gradle.caching.internal.controller.operations.LoadOperationHitResult
import org.gradle.caching.internal.operations.BuildCacheRemoteLoadBuildOperationType
import org.gradle.internal.operations.BuildOperationDescriptor
import org.gradle.internal.operations.OperationFinishEvent
import org.gradle.internal.operations.OperationIdentifier

class BuildCacheOperationListenerTest : BehaviorSpec({
    given("BuildCacheOperationListener") {
        val listenerUnderTest = BuildCacheOperationListener()
        `when`("cache disabled for task") {
            val taskAName = "taskA"
            val taskAIdentifier = OperationIdentifier(1L)
            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskAName).build(taskAIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = "disabled during test"
                    )
                )
            )
            then("report cache disabled for local and remote") {
                listenerUnderTest.get().shouldBe(
                    ExecutedTasksInfo(
                        mapOf(
                            taskAName to
                                    ExecutedGradleTaskInfo(
                                        id = 1,
                                        taskName = taskAName,
                                        isCacheEnabled = false,
                                        localCacheInfo = CacheInfo.CacheDisabled,
                                        remoteCacheInfo = CacheInfo.CacheDisabled
                                    )
                        )
                    )
                )
            }
        }

        `when`("local cache hit") {
            val taskAName = "taskA"
            val taskAIdentifier = OperationIdentifier(1L)
            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskAName).build(taskAIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = null,
                        skipMessage = "FROM-CACHE"
                    )
                )
            )
            then("report hit for local cache and miss for remote cache") {
                listenerUnderTest.get().shouldBe(
                    ExecutedTasksInfo(
                        mapOf(
                            taskAName to
                                    ExecutedGradleTaskInfo(
                                        id = 1,
                                        taskName = taskAName,
                                        isCacheEnabled = true,
                                        localCacheInfo = CacheInfo.CacheHit,
                                        remoteCacheInfo = CacheInfo.CacheMiss
                                    )
                        )
                    )
                )
            }
        }

        `when`("remote cache hit") {
            val taskAName = "taskA"
            val taskAIdentifier = OperationIdentifier(1L)
            val taskACachelLoadIdentifier = OperationIdentifier(2L)

            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskAName).build(taskAIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = null,
                        skipMessage = "FROM-CACHE")
                )
            )

            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName("load remote cache")
                    .build(taskACachelLoadIdentifier, taskAIdentifier),
                createRemoteCacheLoadEvent(LoadOperationHitResult(1L))
            )
            then("report hit for local cache miss and hit for remote cache") {
                listenerUnderTest.get().shouldBe(
                    ExecutedTasksInfo(
                        mapOf(
                            taskAName to
                                    ExecutedGradleTaskInfo(
                                        id = 1,
                                        taskName = taskAName,
                                        isCacheEnabled = true,
                                        localCacheInfo = CacheInfo.CacheMiss,
                                        remoteCacheInfo = CacheInfo.CacheHit
                                    )
                        )
                    )
                )
            }
        }

        `when`("when mix of results for different tasks") {
            val taskAName = "taskA"
            val taskBName = "taskB"
            val taskCName = "taskC"
            val taskAIdentifier = OperationIdentifier(1L)
            val taskBIdentifier = OperationIdentifier(2L)
            val taskCIdentifier = OperationIdentifier(3L)
            val taskCCachelLoadIdentifier = OperationIdentifier(4L)
            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskAName).build(taskAIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = "disabled during test"
                    )
                )
            )

            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskBName).build(taskBIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = null,
                        skipMessage = "FROM-CACHE"
                    )
                )
            )

            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName(taskCName).build(taskCIdentifier, null),
                createFinishEvent(
                    ExecuteTaskBuildOperationTypeResult(
                        cachingDisabledReasonCategory = null,
                        skipMessage = "FROM-CACHE")
                )
            )

            listenerUnderTest.finished(
                BuildOperationDescriptor.displayName("load remote cache")
                    .build(taskCCachelLoadIdentifier, taskCIdentifier),
                createRemoteCacheLoadEvent(LoadOperationHitResult(1L))
            )
            then("each task reported correctly") {
                listenerUnderTest.get().shouldBe(
                    ExecutedTasksInfo(
                        mapOf(
                            taskAName to
                                    ExecutedGradleTaskInfo(
                                        id = 1,
                                        taskName = taskAName,
                                        isCacheEnabled = false,
                                        localCacheInfo = CacheInfo.CacheDisabled,
                                        remoteCacheInfo = CacheInfo.CacheDisabled
                                    ),
                            taskBName to
                                    ExecutedGradleTaskInfo(
                                        id = 2,
                                        taskName = taskBName,
                                        isCacheEnabled = true,
                                        localCacheInfo = CacheInfo.CacheHit,
                                        remoteCacheInfo = CacheInfo.CacheMiss
                                    ),
                            taskCName to
                                    ExecutedGradleTaskInfo(
                                        id = 3,
                                        taskName = taskCName,
                                        isCacheEnabled = true,
                                        localCacheInfo = CacheInfo.CacheMiss,
                                        remoteCacheInfo = CacheInfo.CacheHit
                                    )
                        )
                    )
                )
            }
        }
    }
})

private fun createFinishEvent(
    result: ExecuteTaskBuildOperationTypeResult
): OperationFinishEvent {
    return OperationFinishEvent(
        1L, 2L,
        null,
        result
    )
}

private class ExecuteTaskBuildOperationTypeResult(
    private val cachingDisabledReasonCategory: String? = null,
    private val originExecutionTime: Long? = null,
    private val skipMessage: String? = null,
    private val isActionable: Boolean = true,
    private val cachingDisabledReasonMessage: String? = null,
    private val upToDateMessages: MutableList<String> = mutableListOf(),
    private val isIncremental: Boolean = false,
    private val originBuildInvocationId: String? = null
) : ExecuteTaskBuildOperationType.Result {
    override fun getCachingDisabledReasonCategory(): String? = cachingDisabledReasonCategory

    override fun getOriginExecutionTime(): Long? = originExecutionTime

    override fun isActionable(): Boolean = isActionable

    override fun getSkipMessage(): String? = skipMessage

    override fun getCachingDisabledReasonMessage(): String? = cachingDisabledReasonMessage

    override fun getUpToDateMessages(): MutableList<String> = upToDateMessages

    override fun isIncremental(): Boolean = isIncremental

    override fun getOriginBuildInvocationId(): String? = originBuildInvocationId
}

private fun createRemoteCacheLoadEvent(
    result: BuildCacheRemoteLoadBuildOperationType.Result
): OperationFinishEvent {
    return OperationFinishEvent(1L, 2L, null, result)
}
