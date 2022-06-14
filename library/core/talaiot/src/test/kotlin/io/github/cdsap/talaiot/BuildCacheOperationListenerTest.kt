package io.github.cdsap.talaiot

import io.github.cdsap.talaiot.entities.CacheInfo
import io.github.cdsap.talaiot.entities.ExecutedGradleTaskInfo
import io.github.cdsap.talaiot.entities.ExecutedTasksInfo
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.internal.tasks.execution.ExecuteTaskBuildOperationType
import org.gradle.caching.internal.controller.operations.LoadOperationHitResult
import org.gradle.caching.internal.operations.BuildCacheRemoteLoadBuildOperationType
import org.gradle.internal.operations.BuildOperationDescriptor
import org.gradle.internal.operations.OperationFinishEvent
import org.gradle.internal.operations.OperationIdentifier

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
