package com.cdsap.talaiot.entities

data class ExecutedTasksInfo(
    val executedTasksInfo: Map<String, ExecutedGradleTaskInfo>
)

data class ExecutedGradleTaskInfo(
    val id: Long,
    val taskName: String,
    val isCacheEnabled: Boolean,
    val localCacheInfo: CacheInfo,
    val remoteCacheInfo: CacheInfo
)

sealed class CacheInfo {
    object CacheDisabled: CacheInfo()
    object CacheMiss: CacheInfo()
    object CacheHit: CacheInfo()
}
