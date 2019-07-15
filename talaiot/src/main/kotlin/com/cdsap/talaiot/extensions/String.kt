package com.cdsap.talaiot.extensions

import java.math.BigDecimal
import java.util.regex.Pattern

fun String.toBytes(): String? {
    val patt = Pattern.compile("([\\d.]+)([GMK])B?", Pattern.CASE_INSENSITIVE)
    val matcher = patt.matcher(this)
    val powerMap = mapOf(
        "G" to 3,
        "M" to 2,
        "K" to 1
    )
    if (matcher.find()) {
        val number = matcher.group(1)

        val pow = when (matcher.group(2).toUpperCase()) {
            "G" -> 3
            "M" -> 2
            "K" -> 1
            else -> return null
        }

        var bytes = BigDecimal(number)
        bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow))
        return bytes.toLong().toString()
    }
    return null
}