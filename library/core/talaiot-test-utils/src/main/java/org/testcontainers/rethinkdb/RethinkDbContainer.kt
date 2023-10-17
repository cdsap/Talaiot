package org.testcontainers.rethinkdb

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.Base58
import java.net.InetSocketAddress

/**
 * Custom TestContainer to support E2E tests for RethinkDb
 */
open class RethinkDbContainer :
    GenericContainer<RethinkDbContainer>("$RETHINKDB_DEFAULT_IMAGE:$RETHINKDB_DEFAULT_VERSION") {
    init {

        logger().info("Starting an RethinkDb container using [{}]", dockerImageName)
        withNetworkAliases("rethinkdb-" + Base58.randomString(6))
        withEnv("discovery.type", "single-node")
        addExposedPorts(
            RETHINKDB_DEFAULT_PORT,
            RETHINKDB_DEFAULT_TCP_PORT
        )
        setWaitStrategy(
            WaitAllStrategy()
                .withStrategy(
                    Wait.forListeningPort()
                )
        )
    }

    val httpHostAddress: String
        get() = containerIpAddress + ":" + getMappedPort(RETHINKDB_DEFAULT_PORT)

    fun getTcpHost(): InetSocketAddress {
        return InetSocketAddress(containerIpAddress, getMappedPort(RETHINKDB_DEFAULT_TCP_PORT)!!)
    }

    companion object {

        /**
         * RethinkDb Default HTTP port
         */
        private val RETHINKDB_DEFAULT_PORT = 28015

        /**
         * RethinkDb Docker base URL
         */
        private val RETHINKDB_DEFAULT_IMAGE = "rethinkdb"
        private val RETHINKDB_DEFAULT_TCP_PORT = 28015

        /**
         * RethinkDb Default version
         */
        protected val RETHINKDB_DEFAULT_VERSION = "latest"
    }
}
