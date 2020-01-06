package com.cdsap.talaiot.extensions

import java.math.BigDecimal
import java.util.regex.Pattern

fun String.toBytes(): String? {
    val pattern = Pattern.compile("([\\d.]+)([GMK])B?", Pattern.CASE_INSENSITIVE)
    val matcher = pattern.matcher(this)
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