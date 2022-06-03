package io.github.cdsap.talaiot.metrics

enum class TaskMetrics : Metrics {
    State,
    Module,
    RootNode,
    Task,
    Value,
    CacheEnabled,
    Custom {
        override val isCustom: Boolean = true
    };

    override fun toKey(): String = toString()

    override fun toString(): String = super.toString().decapitalize()
}
