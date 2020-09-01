package com.cdsap.talaiot.e2e

import com.cdsap.talaiot.publisher.KRedisContainer
import com.cdsap.talaiot.publisher.graphpublisher.KInfluxDBContainer
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert.assertTrue
import org.gradle.testkit.runner.GradleRunner
import org.influxdb.dto.Query

/**
 * E2E Test to check the if the event BuildCacheRemoteLoadBuildOperationType is emitted by Gradle.
 * We want to check the output of the build but as well the correct behavior of Talaiot with a given Publisher.
 */
class BuildCacheRemoteLoadBuildOperationTypeTest : BehaviorSpec() {
    private val containerRedis = KRedisContainer()
    private val containerInfluxDb = KInfluxDBContainer().withAuthEnabled(false)

    val influxDB by lazy {
        containerInfluxDb.newInfluxDB
    }

    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        containerRedis.start()
        containerInfluxDb.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        containerRedis.stop()
        containerInfluxDb.stop()
    }

    init {
        given("A project with Talaiot and Remote Caching enabled using Redis as Remote Caching") {

            val testProjectDir = TemporaryFolder()
            testProjectDir.create()

            testProjectDir.newFile("gradle.properties")
                .appendText(Configuration.gradleProperties)

            testProjectDir.newFile("settings.gradle")
                .appendText(Configuration.settingsGradle(containerRedis.httpHostAddress))

            testProjectDir.newFile("build.gradle")
                .appendText(Configuration.buildGradle(containerInfluxDb.url))

            testProjectDir.newFileInPath("src/main/java/A.java")
                .appendText(Configuration.createFile())

            `when`("there are a sequence of execution for tasks: assemble, clean, assemble again") {
                val firstAssembleExecution = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()

                val cleanExecution = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("clean")
                    .withPluginClasspath()
                    .build()

                val secondAssembleExecution = GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()

                then("First assemble execution feed the cache and Second assemble execution contains build Info with the task state coming from cache") {

                    // Just assert the output to check the state of the tasks
                    assertTrue(!firstAssembleExecution.output.contains("FROM-CACHE"))
                    assertTrue(secondAssembleExecution.output.contains("FROM-CACHE"))

                    // Talaiot was configured with InfluxDb.
                    // Let's check if the cacheable task exists in the two executions with the different states
                    // and test the caching properties(remoteCacheHit) have been emitted properly from Gradle
                    val taskResultTask =
                        influxDB.query(Query("select * from tracking.rpTalaiot.task where task=':compileJava'"))

                    val firstExecution =
                        taskResultTask.results.joinToString { it.series.joinToString { it.values[0].joinToString() } }
                    val secondExecution =
                        taskResultTask.results.joinToString { it.series.joinToString { it.values[1].joinToString() } }

                    assert(taskResultTask.results[0].series[0].values.size == 2)
                    assert(firstExecution.contains("EXECUTED"))
                    assert(secondExecution.contains("FROM_CACHE"))

                    val remoteCacheHit =
                        influxDB.query(Query("select * from tracking.rpTalaiot.task where remoteCacheHit='true'"))

                    assert(remoteCacheHit.results[0].series[0].values.size == 1)

                }
                testProjectDir.delete()
            }
        }
    }

}

object Configuration {
    val gradleProperties = "org.gradle.caching=true"
    fun settingsGradle(containerUrl: String) = """
        buildscript {
            repositories {
                mavenLocal()
                maven {
                    url 'https://plugins.gradle.org/m2/'
                }
            }

            dependencies {
                classpath 'gradle.plugin.net.idlestate:gradle-redis-build-cache:1.2.1'
            }
        }

        buildCache {
            local {
                enabled = false
            }
            registerBuildCacheService(net.idlestate.gradle.caching.RedisBuildCache.class,
                    net.idlestate.gradle.caching.RedisBuildCacheServiceFactory.class)

            remote( net.idlestate.gradle.caching.RedisBuildCache.class ) {
                host = 'localhost'
                port = ${containerUrl.split(":")[1]}
                enabled = true
                push = true
            }
        }
        
    """.trimIndent()

    fun buildGradle(containerUrl: String) = """
        plugins {
            id 'java'
            id 'com.cdsap.talaiot'
        }
                 
        talaiot{
            publishers {
                logger = com.cdsap.talaiot.logger.LogTracker.Mode.INFO                      
                influxDbPublisher { 
                    dbName = "tracking"
                    url = "$containerUrl"
                    taskMetricName = "task"
                    buildMetricName = "build"
                }
            }
        }        
    """.trimIndent()

    fun createFile() = """
        class A {
            public A(){
            }
        }
    """.trimIndent()
}
