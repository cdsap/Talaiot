package com.cdsap.talaiot.metrics

enum class TaskMetrics : Metrics {
    State,
    Module,
    RootNode,
    Task,
    WorkerId,
    Critical,
    Value,
    CacheEnabled,
    LocalCacheHit,
    LocalCacheMiss,
    RemoteCacheHit,
    RemoteCacheMiss,
    Custom {
        override val isCustom: Boolean = true
    };

    override fun toKey(): String = toString()

    override fun toString(): String = super.toString().decapitalize()
}