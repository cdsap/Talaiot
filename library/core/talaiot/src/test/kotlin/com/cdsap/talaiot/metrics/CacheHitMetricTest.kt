package com.cdsap.talaiot.metrics

import com.cdsap.talaiot.entities.CacheInfo
import com.cdsap.talaiot.entities.Environment
import com.cdsap.talaiot.entities.ExecutedGradleTaskInfo
import com.cdsap.talaiot.entities.ExecutedTasksInfo
import com.cdsap.talaiot.entities.ExecutionReport
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec

class CacheHitMetricTest : BehaviorSpec({
    given("CacheHitMetrics") {
        val cacheHitMetrics = CacheHitMetric()
        `when`("no cache info is provided") {
            val report = ExecutionReport(
                environment = Environment()
            )
            cacheHitMetrics.get(ExecutedTasksInfo(emptyMap()), report)
            then("all aggregations are zero") {
                report.environment.localCacheHit.shouldBe(0)
                report.environment.localCacheMiss.shouldBe(0)
                report.environment.remoteCacheHit.shouldBe(0)
                report.environment.remoteCacheMiss.shouldBe(0)

            }
        }

        `when`("cache info is provided") {
            val report = ExecutionReport(
                environment = Environment()
            )
            cacheHitMetrics.get(
                ExecutedTasksInfo(
                    mapOf(
                        "a" to ExecutedGradleTaskInfo(1L, "a", true, CacheInfo.CacheHit, CacheInfo.CacheMiss),
                        "b" to ExecutedGradleTaskInfo(1L, "b", true, CacheInfo.CacheMiss, CacheInfo.CacheMiss),
                        "c" to ExecutedGradleTaskInfo(1L, "c", true, CacheInfo.CacheMiss, CacheInfo.CacheHit),
                        "d" to ExecutedGradleTaskInfo(1L, "d", true, CacheInfo.CacheDisabled, CacheInfo.CacheMiss),
                        "e" to ExecutedGradleTaskInfo(1L, "e", true, CacheInfo.CacheDisabled, CacheInfo.CacheHit),
                        "f" to ExecutedGradleTaskInfo(1L, "f", true, CacheInfo.CacheDisabled, CacheInfo.CacheHit),
                        "g" to ExecutedGradleTaskInfo(1L, "g", true, CacheInfo.CacheDisabled, CacheInfo.CacheHit)
                    )
                ),
                report
            )
            then("should aggregate hits and misses") {
                report.environment.localCacheHit.shouldBe(1)
                report.environment.localCacheMiss.shouldBe(2)
                report.environment.remoteCacheHit.shouldBe(4)
                report.environment.remoteCacheMiss.shouldBe(3)
            }
        }
    }
})
