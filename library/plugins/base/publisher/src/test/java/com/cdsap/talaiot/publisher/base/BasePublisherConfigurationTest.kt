package com.cdsap.talaiot.publisher.base

import io.kotlintest.specs.BehaviorSpec

class BasePublisherConfigurationTest : BehaviorSpec({
    given("Any of the Publisher configuration") {

        `when`("There is no additional setup") {
            val outputPublisherConfiguration = OutputPublisherConfiguration()
            then("Publish tasks and build by default") {
                assert(outputPublisherConfiguration.publishTaskMetrics)
                assert(outputPublisherConfiguration.publishBuildMetrics)
            }
        }

    }
})