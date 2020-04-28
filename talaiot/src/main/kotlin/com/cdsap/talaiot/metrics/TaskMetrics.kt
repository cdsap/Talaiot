package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.CacheInfo
import com.cdsap.talaiot.metrics.base.ExecutedTasksMetric

class CacheHitMetric : ExecutedTasksMetric<BuildCacheUsageStats>(
    provider = { info ->
        var localCacheHitCount = 0
        var localCacheMissCount = 0
        var remoteCacheHitCount = 0
        var remoteCacheMissCount = 0
        info.executedTasksInfo.values.forEach {
            when (it.localCacheInfo) {
                is CacheInfo.CacheHit -> localCacheHitCount++
                is CacheInfo.CacheMiss -> localCacheMissCount++
                CacheInfo.CacheDisabled -> {
                }
            }

            when (it.remoteCacheInfo) {
                is CacheInfo.CacheHit -> remoteCacheHitCount++
                is CacheInfo.CacheMiss -> remoteCacheMissCount++
                CacheInfo.CacheDisabled -> {
                }
            }
        }
        BuildCacheUsageStats(
            localCacheHit = localCacheHitCount,
            localCacheMiss = localCacheMissCount,
            remoteCacheHit = remoteCacheHitCount,
            remoteCacheMiss = remoteCacheMissCount
        )
    },
    assigner = { report, value ->
        report.environment.localCacheHit = value.localCacheHit
        report.environment.localCacheMiss = value.localCacheMiss
        report.environment.remoteCacheHit = value.remoteCacheHit
        report.environment.remoteCacheMiss = value.remoteCacheMiss
    }
)

data class BuildCacheUsageStats(
    val localCacheHit: Int,
    val localCacheMiss: Int,
    val remoteCacheHit: Int,
    val remoteCacheMiss: Int
)
