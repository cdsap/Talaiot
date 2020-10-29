package com.cdsap.talaiot.assertions

import io.kotlintest.Matcher
import io.kotlintest.Result
import io.kotlintest.shouldBe
import kotlin.reflect.KClass

private fun <T : Any> containExactlyTypesOfInAnyOrder(ts: Collection<KClass<*>>): Matcher<Collection<T>> =
    object : Matcher<Collection<T>> {
        override fun test(value: Collection<T>) = Result(
            ts.size == value.size && value.all { ts.contains(it::class) },
            "Collection ${value.joinToString(", ")} should contain all of ${ts.joinToString(", ")}",
            "Collection ${value.joinToString(", ")} should not contain all of ${ts.joinToString(", ")}"
        )
    }

fun <T : Any> Collection<T>.shouldContainExactlyTypesOfInAnyOrder(ts: Collection<KClass<*>>) {
    this shouldBe containExactlyTypesOfInAnyOrder<T>(ts)
}
