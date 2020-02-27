package com.cdsap.talaiot.configuration

import io.kotlintest.specs.BehaviorSpec

class PublisherConfigurationTest : BehaviorSpec({
    given("Any of the Publisher configuration") {

        `when`("There is no additional setup") {
            val pushGatewayPublisherConfiguration = PushGatewayPublisherConfiguration()
            val influxDbPublisherConfiguration = InfluxDbPublisherConfiguration()
            val outputPublisherConfiguration = OutputPublisherConfiguration()
            val elasticSearchPublisherConfiguration = ElasticSearchPublisherConfiguration()
            val hybridPublisherConfiguration = HybridPublisherConfiguration()
            val rethinkDbPublisherConfiguration = RethinkDbPublisherConfiguration()
            then("Publish tasks and build by default") {
                assert(pushGatewayPublisherConfiguration.publishBuildMetrics)
                assert(pushGatewayPublisherConfiguration.publishTaskMetrics)
                assert(influxDbPublisherConfiguration.publishBuildMetrics)
                assert(influxDbPublisherConfiguration.publishTaskMetrics)
                assert(outputPublisherConfiguration.publishBuildMetrics)
                assert(outputPublisherConfiguration.publishTaskMetrics)
                assert(elasticSearchPublisherConfiguration.publishBuildMetrics)
                assert(elasticSearchPublisherConfiguration.publishTaskMetrics)
                assert(hybridPublisherConfiguration.publishBuildMetrics)
                assert(hybridPublisherConfiguration.publishTaskMetrics)
                assert(rethinkDbPublisherConfiguration.publishTaskMetrics)
                assert(rethinkDbPublisherConfiguration.publishBuildMetrics)
            }
        }

    }
})