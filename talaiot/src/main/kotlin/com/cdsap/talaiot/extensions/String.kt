package com.cdsap.talaiot.extensions

import java.math.BigDecimal
import java.util.HashMap
import java.util.regex.Pattern

fun String.toBytes(): String? {
    val patt = Pattern.compile("([\\d.]+)([GMK])B?", Pattern.CASE_INSENSITIVE)
    val matcher = patt.matcher(this)
    val powerMap = HashMap<String, Int>()
    powerMap["G"] = 3
    powerMap["M"] = 2
    powerMap["K"] = 1
    if (matcher.find()) {
        val number = matcher.group(1)
        val pow = powerMap[matcher.group(2).toUpperCase()]!!
        var bytes = BigDecimal(number)
        bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow))
        return bytes.toLong().toString()
    }
    return null
}