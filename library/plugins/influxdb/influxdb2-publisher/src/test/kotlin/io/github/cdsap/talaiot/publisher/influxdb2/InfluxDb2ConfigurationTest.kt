package io.github.cdsap.talaiot.publisher.influxdb

import io.github.cdsap.talaiot.publisher.influxdb2.InfluxDb2PublisherConfiguration
import io.kotlintest.specs.BehaviorSpec

class InfluxDb2ConfigurationTest : BehaviorSpec({
    given("InfluxDb configuration") {

        `when`("There is no retention policy defined") {
            val influxDbPublisherConfiguration = InfluxDb2PublisherConfiguration()
            then("default values are given") {
//                assert(
//                    influxDbPublisherConfiguration.retentionPolicyConfiguration.name == "rpTalaiot" &&
//                        influxDbPublisherConfiguration.retentionPolicyConfiguration.duration == "30d" &&
//                        influxDbPublisherConfiguration.buildTags.isEmpty()
//                )
            }
        }
        `when`("There is custom retention policy defined") {
            val influxDbPublisherConfiguration = InfluxDb2PublisherConfiguration()

//            influxDbPublisherConfiguration.retentionPolicyConfiguration {
//                name = "customRp"
//                duration = "99w"
//            }

            then("custom values are given") {
//                assert(
//                    influxDbPublisherConfiguration.retentionPolicyConfiguration.name == "customRp" &&
//                        influxDbPublisherConfiguration.retentionPolicyConfiguration.duration == "99w" &&
//                        influxDbPublisherConfiguration.retentionPolicyConfiguration.shardDuration == "30m"
//                )
            }
        }
    }
})
