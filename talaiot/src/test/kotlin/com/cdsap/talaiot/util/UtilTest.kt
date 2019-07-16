package com.cdsap.talaiot.util

import com.cdsap.talaiot.extensions.toBytes
import io.kotlintest.specs.BehaviorSpec


class UtilTest : BehaviorSpec({

    given("some memory conversion") {
        `when`("pass valid Xmx") {
            val xmxBytes = "-Xmx2G".toBytes()

            then("it should return bytes") {
                assert(xmxBytes == (1024L * 1024 * 1024 * 2).toString())
            }
        }
    }
})
