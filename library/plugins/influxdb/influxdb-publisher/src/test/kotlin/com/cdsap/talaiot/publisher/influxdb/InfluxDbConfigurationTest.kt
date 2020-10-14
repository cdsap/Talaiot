package com.cdsap.talaiot.publisher.influxdb

import com.cdsap.talaiot.publisher.influxdb.InfluxDbPublisherConfiguration
import io.kotlintest.specs.BehaviorSpec

class InfluxDbConfigurationTest : BehaviorSpec({
    given("InfluxDb configuration") {

        `when`("There is no retention policy defined") {
            val influxDbPublisherConfiguration = InfluxDbPublisherConfiguration()
            then("default values are given") {
                assert(
                    influxDbPublisherConfiguration.retentionPolicyConfiguration.name == "rpTalaiot"
                            && influxDbPublisherConfiguration.retentionPolicyConfiguration.duration == "30d"
                )
            }
        }
        `when`("There is custom retention policy defined") {
            val influxDbPublisherConfiguration = InfluxDbPublisherConfiguration()
            influxDbPublisherConfiguration.retentionPolicyConfiguration {
                name = "customRp"
                duration = "99w"
            }

            then("custom values are given") {
                assert(
                    influxDbPublisherConfiguration.retentionPolicyConfiguration.name == "customRp"
                            && influxDbPublisherConfiguration.retentionPolicyConfiguration.duration == "99w"
                            && influxDbPublisherConfiguration.retentionPolicyConfiguration.shardDuration == "30m"
                )
            }
        }
    }
})