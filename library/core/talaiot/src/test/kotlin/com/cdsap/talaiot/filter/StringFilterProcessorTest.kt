package com.cdsap.talaiot.filter

import com.cdsap.talaiot.logger.TestLogTrackerRecorder
import io.kotlintest.matchers.boolean.shouldBeFalse
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.specs.BehaviorSpec

class StringFilterProcessorTest : BehaviorSpec({
    given("A StringFilter configuration") {
        val logTracker = TestLogTrackerRecorder
        `when`("Includes configuration is provided") {

            val includesFilter = StringFilter().apply {
                includes = arrayOf("t1")
            }

            then("The filter processor matches when the provided string matches the inclusion rule") {
                StringFilterProcessor(includesFilter, logTracker).matches("t1").shouldBeTrue()
            }
            then("The filter processor does not match when the provided string does  not match the inclusion rule ") {
                StringFilterProcessor(includesFilter, logTracker).matches("t111").shouldBeFalse()
            }
        }
        `when`("Excludes configuration is provided") {

            val excludesFilter = StringFilter().apply {
                excludes = arrayOf("t1")
            }

            then("The filter processor does not match when provided string matches the exclusion rule") {
                StringFilterProcessor(excludesFilter, logTracker).matches("t1").shouldBeFalse()

            }
            then("The filter processor matches when provided string does not match the exclusion rule") {
                StringFilterProcessor(excludesFilter, logTracker).matches("t111").shouldBeTrue()
            }
        }
        `when`("Excludes and includes configuration are provided") {

            val compositeFilter = StringFilter().apply {
                excludes = arrayOf("t1.*")
                includes = arrayOf("t2.*")

            }

            then("The filter processor does not match when provided string matches the exclusion rule") {
                StringFilterProcessor(compositeFilter, logTracker).matches("t1").shouldBeFalse()
                StringFilterProcessor(compositeFilter, logTracker).matches("t123").shouldBeFalse()
                StringFilterProcessor(compositeFilter, logTracker).matches("t1fdd").shouldBeFalse()
                StringFilterProcessor(compositeFilter, logTracker).matches("t1qwer").shouldBeFalse()

            }
            then("The filter processor matches when provided string does not match the exclusion rule") {
                StringFilterProcessor(compositeFilter, logTracker).matches("t2af").shouldBeTrue()
                StringFilterProcessor(compositeFilter, logTracker).matches("t2.gf").shouldBeTrue()
                StringFilterProcessor(compositeFilter, logTracker).matches("t2-ta").shouldBeTrue()
                StringFilterProcessor(compositeFilter, logTracker).matches("t2t1").shouldBeTrue()
            }
        }

        `when`("Excludes and includes configuration are provided and are the same") {

            val includesFilter = StringFilter().apply {
                excludes = arrayOf("t1.*")
                includes = arrayOf("t1.*")

            }

            then("The filter processor does not match when provided string matches the exclusion rule and logtracker records an error") {
                StringFilterProcessor(includesFilter, logTracker).matches("t1").shouldBeFalse()
                logTracker.containsLog("t1 matches with inclusion and exclusion filter").shouldBeTrue()

            }
        }
    }
})