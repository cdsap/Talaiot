package org.testcontainers.pushgateway

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.Base58
import java.net.InetSocketAddress

/**
 * Custom TestContainer to support E2E tests for PushGateway
 */
open class PushGatewayContainer :
    GenericContainer<PushGatewayContainer>("$PUSHGATEWAY_DEFAULT_IMAGE:$PUSHGATEWAY_DEFAULT_VERSION") {
    init {

        logger().info("Starting an Pushgateway container using [{}]", dockerImageName)
        withNetworkAliases("pushgateway-" + Base58.randomString(6))
        withEnv("discovery.type", "single-node")
        addExposedPorts(
            PUSHGATEWAY_DEFAULT_PORT,
            PUSHGATEWAY_DEFAULT_TCP_PORT
        )
        setWaitStrategy(
            WaitAllStrategy()
                .withStrategy(
                    Wait.forListeningPort()
                )
        )
    }

    val httpHostAddress: String
        get() = containerIpAddress + ":" + getMappedPort(PUSHGATEWAY_DEFAULT_PORT)

    fun getTcpHost(): InetSocketAddress {
        return InetSocketAddress(containerIpAddress, getMappedPort(PUSHGATEWAY_DEFAULT_TCP_PORT)!!)
    }

    companion object {

        /**
         * Pushgateway Default HTTP port
         */
        private val PUSHGATEWAY_DEFAULT_PORT = 9091

        /**
         * Pushgateway Docker base URL
         */
        private val PUSHGATEWAY_DEFAULT_IMAGE = "prom/pushgateway"
        private val PUSHGATEWAY_DEFAULT_TCP_PORT = 9091

        /**
         * Pushgateway Default version
         */
        protected val PUSHGATEWAY_DEFAULT_VERSION = "latest"
    }
}
