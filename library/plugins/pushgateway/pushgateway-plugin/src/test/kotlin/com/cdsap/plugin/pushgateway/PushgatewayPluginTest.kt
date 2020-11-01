package com.cdsap.plugin.pushgateway


import com.cdsap.talaiot.utils.TemporaryFolder
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
                      id 'com.cdsap.talaiot.plugin.rethinkdb'
                   }

                  talaiot {
                    publishers {
                      rethinkDbPublisher {
                             url = "http://"${container.httpHostAddress}
                      }
                    }
                  }
            """
                )

                GradleRunner.create()
                    .withProjectDir(testProjectDir.getRoot())
                    .withArguments("assemble")
                    .withPluginClasspath()
                    .build()
                then("there are build/task records in the Pushgateway instance") {
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
                    assert(
                        content?.contains(":assemble{")
                            ?: false
                    )
                    assert(
                        content?.contains(":clean{cacheEnabled=\"false\",critical=\"false\",instance=\"\",job=\"task\",localCacheHit=\"false\",localCacheMiss=\"false\",metric1=\"value1\",metric2=\"value2\",module=\"app\",remoteCacheHit=\"false\",remoteCacheMiss=\"false\",rootNode=\"false\",state=\"EXECUTED\",task=\":clean\",value=\"1\",workerId=\"\"} 1")
                            ?: false
                    )
                    assert(
                        content?.contains("build")
                            ?: false
                    )
                }
                testProjectDir.delete()
            }

        }
    }
}
