package com.cdsap.talaiot.util

import io.kotlintest.specs.BehaviorSpec


class TalaiotPublisherImplTest : BehaviorSpec({

    given("some memory conversion") {
        `when`("pass valid Xmx") {
            val xmxBytes = Util.toBytes("-Xmx2G")

            then("it should return bytes") {
                assert(xmxBytes == 1024L * 1024 * 2)
            }
        }
    }
})
