package com.cdsap.talaiot.metrics

import io.kotlintest.matchers.maps.shouldContainAll
import io.kotlintest.properties.Gen
import io.kotlintest.properties.assertAll
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec


class MetricsParserTest : StringSpec() {
    init {
        "String size" {
//            forAll(PersonGenerator().random(), { list: List<Metrics> ->
//                MetricsParser(list).get() shouldContainAll list.fold(mutableMapOf()) { acc, f ->
//                    (f.get().toMutableMap() + acc.toMutableMap()) as MutableMap<String, String>
//                }}
//            })
        }
    }
}

class PersonGenerator : Gen<TesrtMetrics> {
    override fun constants() = emptyList<TesrtMetrics>()
    override fun random() = generateSequence {
        TesrtMetrics(
            Gen.map(
                Gen.string(),
                Gen.string()
            ).random().first()
        )
    }
}

class TesrtMetrics(val map: Map<String, String>) : Metrics {
    override fun get(): Map<String, String> {
        return map
    }

}

data class Person(val name: String, val age: Int)
class PersonsGenerator : Gen<Person> {
    override fun constants() = emptyList<Person>()
    override fun random() = generateSequence {
        Person(Gen.string().random().first(), Gen.int().random().first())
    }
}