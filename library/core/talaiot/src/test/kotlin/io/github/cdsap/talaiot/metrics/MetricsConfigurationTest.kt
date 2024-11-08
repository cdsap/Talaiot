package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.assertions.shouldContainExactlyTypesOfInAnyOrder
import io.github.cdsap.talaiot.configuration.MetricsConfiguration
import io.github.cdsap.talaiot.entities.CustomProperties
import io.github.cdsap.talaiot.entities.ExecutionReport
import io.github.cdsap.talaiot.mock.AdbVersionMetric
import io.github.cdsap.talaiot.mock.KotlinVersionMetric
import io.kotlintest.shouldBe
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testfixtures.ProjectBuilder

class MetricsConfigurationTest : BehaviorSpec({
    given("metrics configuration") {
        val target = ProjectBuilder.builder().build()

        `when`("configuration is not changed") {
            val metricsConfiguration = MetricsConfiguration()
            val metrics = metricsConfiguration.build(target)
            then("included all metrics") {
                val expectedMetricsTypes = defaultMetricsTypes +
                    environmentMetricsTypes +
                    performanceMetricsTypes +
                    gradleSwitchesMetricsTypes +
                    gitMetricsTypes
                metrics.shouldContainExactlyTypesOfInAnyOrder(expectedMetricsTypes)
            }
        }

        `when`("only default metrics are configured") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = true
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("only default metrics are included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(defaultMetricsTypes)
            }
        }

        `when`("environment metrics are configured") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = true
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("only environment metrics are included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(environmentMetricsTypes)
            }
        }
        `when`("only performance metrics are configured") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = true
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("only performance metrics are included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(performanceMetricsTypes)
            }
        }
        `when`("git metrics are configured") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = true
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("only git metrics are included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(gitMetricsTypes)
            }
        }

        `when`("gradle switches metrics are configured") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = true
                environmentMetrics = false
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("only gradle switches metrics is included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(gradleSwitchesMetricsTypes)
            }
        }

        `when`("build Id generation is disabled in the default behaviour") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = true
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("BuildIdMetric is not included") {
                assert(metrics.count { it is BuildIdMetric } == 0)
                metrics.shouldContainExactlyTypesOfInAnyOrder(performanceMetricsTypes)
            }
        }
        `when`("build Id generation is enabled") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = true
                gradleSwitchesMetrics = false
                environmentMetrics = false
                generateBuildId = true
                processMetrics = false
            }
            val metrics = metricsConfiguration.build(target)
            then("BuildIdMetric is included") {
                val expectedMetricsTypes = performanceMetricsTypes + BuildIdMetric::class
                metrics.shouldContainExactlyTypesOfInAnyOrder(expectedMetricsTypes)
            }
        }

        `when`("single custom metric is used") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false

                customMetrics(AdbVersionMetric())
            }
            val metrics = metricsConfiguration.build(target)
            then("AdbMetric is included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(listOf(AdbVersionMetric::class))
            }
        }
        `when`("two custom metrics are used") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false

                customMetrics(
                    AdbVersionMetric(),
                    KotlinVersionMetric()
                )
            }
            val metrics = metricsConfiguration.build(target)
            then("AdbMetric and KotlinVersionMetric are included") {
                val expectedMetricsTypes = listOf(AdbVersionMetric::class, KotlinVersionMetric::class)
                metrics.shouldContainExactlyTypesOfInAnyOrder(expectedMetricsTypes)
            }
        }
        `when`("defaults and custom metrics are used") {
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = true
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false

                customMetrics(
                    KotlinVersionMetric()
                )
            }
            val metrics = metricsConfiguration.build(target)
            then("default metrics and KotlinVersionMetric are included") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(defaultMetricsTypes + KotlinVersionMetric::class)
            }
        }

        `when`("custom build metrics are used") {
            val expectedBuildProperties = mutableMapOf(
                "metricA" to "valueA",
                "metricB" to "valueB"
            )
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false

                customBuildMetrics(expectedBuildProperties)
            }
            val metrics = metricsConfiguration.build(target)
            then("add custom build metrics to the report") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(listOf(SimpleMetric::class, SimpleMetric::class))
                val resultingReport = ExecutionReport()
                metrics.forEach {
                    when (it) {
                        is SimpleMetric -> it.get(Unit, resultingReport)
                    }
                }
                resultingReport.customProperties.shouldBe(
                    CustomProperties(
                        expectedBuildProperties,
                        mutableMapOf()
                    )
                )
            }
        }

        `when`("custom task metrics are used") {
            val expectedTaskProperties = mutableMapOf(
                "metricA" to "valueA",
                "metricB" to "valueB"
            )
            val metricsConfiguration = MetricsConfiguration().apply {
                defaultMetrics = false
                gitMetrics = false
                performanceMetrics = false
                gradleSwitchesMetrics = false
                environmentMetrics = false
                processMetrics = false

                customTaskMetrics(expectedTaskProperties)
            }
            val metrics = metricsConfiguration.build(target)
            then("add custom build metrics to the report") {
                metrics.shouldContainExactlyTypesOfInAnyOrder(listOf(SimpleMetric::class, SimpleMetric::class))
                val resultingReport = ExecutionReport()
                metrics.forEach {
                    when (it) {
                        is SimpleMetric -> it.get(Unit, resultingReport)
                    }
                }
                resultingReport.customProperties.shouldBe(
                    CustomProperties(
                        mutableMapOf(),
                        expectedTaskProperties
                    )
                )
            }
        }
    }
})

private val defaultMetricsTypes = listOf(
    RootProjectNameMetric::class,
    GradleRequestedTasksMetric::class,
    GradleVersionMetric::class
)

private val performanceMetricsTypes = listOf(
    ProcessorCountMetric::class,
    UserMetric::class,
    OsMetric::class,
    JavaVmNameMetric::class,
    LocaleMetric::class,
    GradleMaxWorkersMetric::class,
    JvmXmsMetric::class,
    JvmXmxMetric::class,
    JvmMaxPermSizeMetric::class
)

private val environmentMetricsTypes = listOf(
    HostnameMetric::class,
    DefaultCharsetMetric::class
)

private val gitMetricsTypes = listOf(
    GitUserMetric::class
)

private val gradleSwitchesMetricsTypes = listOf(
    GradleSwitchCachingMetric::class,
    GradleSwitchBuildScanMetric::class,
    GradleSwitchParallelMetric::class,
    GradleSwitchConfigureOnDemandMetric::class,
    GradleSwitchDryRunMetric::class,
    GradleSwitchRefreshDependenciesMetric::class,
    GradleSwitchRerunTasksMetric::class,
    GradleSwitchConfigurationCacheMetric::class
)
