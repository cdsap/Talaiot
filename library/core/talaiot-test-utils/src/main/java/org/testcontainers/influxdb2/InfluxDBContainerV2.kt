package org.testcontainers.influxdb2

import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.containers.wait.strategy.WaitAllStrategy
import org.testcontainers.utility.DockerImageName
import java.util.Optional

/**
 * Following code was extracted from https://github.com/testcontainers/testcontainers-java/pull/3669
 * The PR is not merged and implements the InfluxDb 2.x testcontainer component

 * Refer to
 * [ the official InfluxDB 2.x container repository](https://hub.docker.com/_/influxdb)
 * on docker hub for detailed documentation and newest tags.
 */
open class InfluxDBContainerV2(imageName: DockerImageName) : GenericContainer<InfluxDBContainerV2?>(imageName) {
    private var username = "test-user"

    private var password = "test-password"

    private var bucket = "test-bucket"

    private var organization = "test-org"

    private var retention = Optional.empty<String>()

    private var adminToken = "test-token"

    constructor(imageName: String?) : this(DockerImageName.parse(imageName)) {}

    /**
     *
     *
     * The InfluxDB image contains some extra functionality to automatically bootstrap the system. This functionality is
     * enabled by setting the DOCKER_INFLUXDB_INIT_MODE environment variable to the value "setup" when running the
     * container. Additional environment variables are used to configure the setup logic:
     *
     *  *  DOCKER_INFLUXDB_INIT_USERNAME: The username to set for the system's initial super-user (Required).
     *  *  DOCKER_INFLUXDB_INIT_PASSWORD: The password to set for the system's initial super-user (Required).
     *  *  DOCKER_INFLUXDB_INIT_ORG: The name to set for the system's initial organization (Required).
     *  *  DOCKER_INFLUXDB_INIT_BUCKET: The name to set for the system's initial bucket (Required).
     *  *  DOCKER_INFLUXDB_INIT_RETENTION: The duration the system's initial bucket should retain data. If not set,
     * the initial bucket will retain data forever.
     *  *  DOCKER_INFLUXDB_INIT_ADMIN_TOKEN: The authentication token to associate with the system's initial
     * super-user. If not set, a token will be auto-generated by the system.
     *
     * <br></br>
     * See
     * [ full documentation](https://hub.docker.com/_/influxdb)
     */
    override fun configure() {
        addEnv("DOCKER_INFLUXDB_INIT_MODE", "setup")
        addEnv("DOCKER_INFLUXDB_INIT_USERNAME", username)
        addEnv("DOCKER_INFLUXDB_INIT_PASSWORD", password)
        addEnv("DOCKER_INFLUXDB_INIT_ORG", organization)
        addEnv("DOCKER_INFLUXDB_INIT_BUCKET", bucket)
        retention.ifPresent { ret: String? ->
            addEnv(
                "DOCKER_INFLUXDB_INIT_RETENTION",
                ret
            )
        }
        addEnv("DOCKER_INFLUXDB_INIT_ADMIN_TOKEN", adminToken)
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_USERNAME`.
     *
     * @param username The name of a user to be created with no privileges. If `INFLUXDB_BUCKET` is set, this user will
     * be granted read and write permissions for that database.
     * @return a reference to this container instance
     */
    fun withUsername(username: String): InfluxDBContainerV2 {
        this.username = username
        return this
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_PASSWORD`.
     *
     * @param password The password for the user configured with `INFLUXDB_USER`. If this is unset, a random password is
     * generated and printed to standard out.
     * @return a reference to this container instance
     */
    fun withPassword(password: String): InfluxDBContainerV2 {
        this.password = password
        return this
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_ORG`.
     *
     * @param organization The organization for the initial setup of influxDB.
     * @return a reference to this container instance
     */
    fun withOrganization(organization: String): InfluxDBContainerV2 {
        this.organization = organization
        return this
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_BUCKET`.
     *
     * @param bucket Automatically initializes a bucket with the name of this environment variable.
     * @return a reference to this container instance
     */
    fun withBucket(bucket: String): InfluxDBContainerV2 {
        this.bucket = bucket
        return this
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_RETENTION`.
     *
     * @param retention Duration bucket will retain data (0 is infinite, default is 0).
     * @return a reference to this container instance
     */
    fun withRetention(retention: String): InfluxDBContainerV2 {
        this.retention = Optional.of(retention)
        return this
    }

    /**
     * Set env variable `DOCKER_INFLUXDB_INIT_ADMIN_TOKEN`.
     *
     * @param adminToken Authentication token to associate with the admin user.
     * @return a reference to this container instance
     */
    fun withAdminToken(adminToken: String): InfluxDBContainerV2 {
        this.adminToken = adminToken
        return this
    }

    /**
     * @return a url to influxDb
     */
    val url: String
        get() = "http://" + this.host + ":" + getMappedPort(INFLUXDB_PORT)

    companion object {
        private const val INFLUXDB_PORT = 8086
        private val DEFAULT_IMAGE_NAME = DockerImageName.parse("influxdb")
        private const val NO_CONTENT_STATUS_CODE = 204
    }

    init {
        imageName.assertCompatibleWith(DEFAULT_IMAGE_NAME)
        waitStrategy = WaitAllStrategy()
            .withStrategy(
                Wait
                    .forHttp("/ping")
                    .withBasicCredentials(username, password)
                    .forStatusCode(NO_CONTENT_STATUS_CODE)
            )
            .withStrategy(Wait.forListeningPort())
        addExposedPort(INFLUXDB_PORT)
    }
}
