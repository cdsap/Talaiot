package com.cdsap.talaiot.plugin.rethinkdb

import com.cdsap.talaiot.utils.TemporaryFolder
import com.rethinkdb.RethinkDB
import com.rethinkdb.net.Connection
import io.kotlintest.Spec
import io.kotlintest.specs.BehaviorSpec
import junit.framework.Assert
import org.gradle.testkit.runner.GradleRunner
import org.testcontainers.rethinkdb.KRethinkDbContainer
import java.net.URL

class RethinkdbPluginTest : BehaviorSpec() {

    val container = KRethinkDbContainer()
    val r = RethinkDB.r


    override fun beforeSpec(spec: Spec) {
        super.beforeSpec(spec)
        container.start()
    }

    override fun afterSpec(spec: Spec) {
        super.afterSpec(spec)
        container.stop()
    }

    init {
        given("Rethinkdb Talaiot Plugin") {
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
                             url = "http://${container.httpHostAddress}"
                             taskTableName = "tasks"
                             buildTableName = "builds"
                             dbName = "tracking"
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
                then("there are records in the Rethinkdb instance") {
                    // We are testing the plugin and we can't inject
                    // a test scheduler, forcing to sleep
                    Thread.sleep(2000)
                    val conn = getConnection("http://${container.httpHostAddress}")
                    val existsTableTasks =
                        r.db("tracking").tableList().contains("tasks")
                            .run<Boolean>(conn)
                    val existsTableBuilds =
                        r.db("tracking").tableList().contains("builds")
                            .run<Boolean>(conn)
                    Assert.assertTrue(existsTableBuilds)
                    Assert.assertTrue(existsTableTasks)
                }
                testProjectDir.delete()
            }

        }
    }

    private fun getConnection(url: String): Connection {
        val url = URL(url)
        return r.connection().hostname(url.host).port(url.port).connect()
    }

}