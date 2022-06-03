package com.cdsap.plugin.pushgateway

import io.github.cdsap.talaiot.utils.TemporaryFolder
import io.github.rybalkinsd.kohttp.dsl.httpGet
import io.github.rybalkinsd.kohttp.ext.url
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import org.gradle.testkit.runner.GradleRunner
import org.testcontainers.pushgateway.KPushGatewayContainer
import java.net.URL

class PushgatewayPluginTest : BehaviorSpec() {

    val container = KPushGatewayContainer()
    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    init {
        given("Pushgateway Talaiot Plugin") {
            val testProjectDir = TemporaryFolder()
            `when`("Project includes the plugin") {
                testProjectDir.create()
                val buildFile = testProjectDir.newFile("build.gradle")
                buildFile.appendText(
                    """
                   plugins {
                      id 'java'
                      id 'io.github.cdsap.talaiot.plugin.pushgateway'
                   }

                  talaiot {
                    publishers {
                      pushGatewayPublisher {
                             url = "http://${container.httpHostAddress}"
                      }
                    }
                  }
            """
                )

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments(":assemble")
                    .withPluginClasspath()
                    .build()
                then("there are build/task records in the Pushgateway instance") {
                    Thread.sleep(2000)
                    val urlSpec = URL("http://" + container.httpHostAddress + "/metrics")

                    val a = httpGet {
                        url(urlSpec)
                        if (urlSpec.query != null) {
                            val query = urlSpec.query.split("=")
                            param {
                                query[0] to query[1]
                            }
                        }
                    }
                    val content = a.body()?.string()
                    println(content)
                    assert(
                        content?.contains("gradle_build_total_time{")
                            ?: false
                    )
                    assert(
                        content?.contains("requestedTasks=\":assemble\"")
                            ?: false
                    )
                    assert(
                        content?.contains("gradle_task_assemble{")
                            ?: false
                    )
                }
                testProjectDir.delete()
            }
        }
    }
}
