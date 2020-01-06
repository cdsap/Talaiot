package com.cdsap.talaiot.util

import com.cdsap.talaiot.extensions.toBytes
import io.kotlintest.specs.BehaviorSpec


class UtilTest : BehaviorSpec({

    given("some memory conversion") {
        `when`("pass valid Xmx in G") {
            val xmxBytes = "-Xmx2G".toBytes()

            then("it should return bytes") {
                assert(xmxBytes == (1024L * 1024 * 1024 * 2).toString())
            }
        }
        `when`("pass valid Xmx in M") {
            val xmxBytes = "-Xmx2048M".toBytes()

            then("it should return bytes") {
                assert(xmxBytes == (1024L * 1024 * 1024 * 2).toString())
            }
        }
        `when`("pass valid Xmx in K") {
            val xmxBytes = "-Xmx100K".toBytes()

            then("it should return bytes") {
                assert(xmxBytes == (1024L * 100).toString())
            }
        }
        `when`("pass invalid Xmx") {
            val xmxBytes = "-Xmx29YTE".toBytes()

            then("it should not return bytes") {
                assert(xmxBytes == null)
            }
        }
    }
})
