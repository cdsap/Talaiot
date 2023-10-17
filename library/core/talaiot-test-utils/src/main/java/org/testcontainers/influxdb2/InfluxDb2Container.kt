package org.testcontainers.pushgateway

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.Base58
import java.net.InetSocketAddress

/**
 * Custom TestContainer to support E2E tests for PushGateway
 */
open class InfluxDb2Container :
    GenericContainer<PushGatewayContainer>("$INFLUXDB2_DEFAULT_IMAGE:$INFLUXDB2_DEFAULT_VERSION") {
    init {

        logger().info("Starting an  container using [{}]", dockerImageName)
        withNetworkAliases("influxdb2-" + Base58.randomString(6))
        withEnv("discovery.type", "single-node")
        withEnv("bucket", "aaaaaa")
        withEnv("org", "alo")
        addExposedPorts(
            INFLUXDB2_DEFAULT_PORT,
            INFLUXDB2_DEFAULT_TCP_PORT
        )
        setWaitStrategy(
            WaitAllStrategy()
                .withStrategy(
                    Wait.forListeningPort()
                )
        )
    }

    val httpHostAddress: String
        get() = containerIpAddress + ":" + getMappedPort(INFLUXDB2_DEFAULT_PORT)

    fun getTcpHost(): InetSocketAddress {
        return InetSocketAddress(containerIpAddress, getMappedPort(INFLUXDB2_DEFAULT_TCP_PORT)!!)
    }

    companion object {

        /**
         * Pushgateway Default HTTP port
         */
        private val INFLUXDB2_DEFAULT_PORT = 8086

        /**
         * Pushgateway Docker base URL
         */
        private val INFLUXDB2_DEFAULT_IMAGE = "influxdb"
        private val INFLUXDB2_DEFAULT_TCP_PORT = 8086

        /**
         * Pushgateway Default version
         */
        protected val INFLUXDB2_DEFAULT_VERSION = "latest"
    }
}
