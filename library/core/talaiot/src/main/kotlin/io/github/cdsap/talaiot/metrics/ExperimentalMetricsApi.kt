package io.github.cdsap.talaiot.metrics

@RequiresOptIn(
    level = RequiresOptIn.Level.WARNING,
    message = "Initial and Final Metrics with Providers are experimental. " +
        "You can silence this message using @OptIn(ExperimentalMetricsApi::class)."
)
@Retention(AnnotationRetention.BINARY)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.CLASS)
annotation class ExperimentalMetricsApi
