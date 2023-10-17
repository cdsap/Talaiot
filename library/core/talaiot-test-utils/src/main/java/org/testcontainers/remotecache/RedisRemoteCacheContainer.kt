package org.testcontainers.remotecache

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.Base58
import java.net.InetSocketAddress

/**
 * Custom TestContainer to support E2E tests for Redis
 */
open class RedisRemoteCacheContainer :
    GenericContainer<RedisRemoteCacheContainer>("$REDIS_IMAGE:$REDIS_DEFAULT_VERSION") {
    init {

        logger().info("Starting an Redis container using [{}]", dockerImageName)
        withNetworkAliases("redis-" + Base58.randomString(6))
        withEnv("discovery.type", "single-node")
        addExposedPorts(
            REDIS_DEFAULT_PORT,
            REDIS_DEFAULT_TCP_PORT
        )
        setWaitStrategy(
            WaitAllStrategy()
                .withStrategy(
                    Wait.forListeningPort()
                )
        )
    }

    val httpHostAddress: String
        get() = containerIpAddress + ":" + getMappedPort(REDIS_DEFAULT_PORT)

    fun getTcpHost(): InetSocketAddress {
        return InetSocketAddress(containerIpAddress, getMappedPort(REDIS_DEFAULT_TCP_PORT)!!)
    }

    companion object {

        /**
         * Redis Default HTTP port
         */
        private val REDIS_DEFAULT_PORT = 6379

        /**
         * Redis Docker base URL
         */
        private val REDIS_IMAGE = "redis"
        private val REDIS_DEFAULT_TCP_PORT = 6379

        /**
         * Redis Default version
         */
        protected val REDIS_DEFAULT_VERSION = "latest"
    }
}
