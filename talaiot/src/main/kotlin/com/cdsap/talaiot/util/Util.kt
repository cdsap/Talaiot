package com.cdsap.talaiot.util

import java.math.BigDecimal
import java.util.*
import java.util.regex.Pattern

object Util {
    fun toBytes(filesize: String?): Long {
        var returnValue: Long = -1
        val patt = Pattern.compile("([\\d.]+)([GMK])B?", Pattern.CASE_INSENSITIVE)
        val matcher = patt.matcher(filesize)
        val powerMap = HashMap<String, Int>()
        powerMap["G"] = 3
        powerMap["M"] = 2
        powerMap["K"] = 1
        if (matcher.find()) {
            val number = matcher.group(1)
            val pow = powerMap[matcher.group(2).toUpperCase()]!!
            var bytes = BigDecimal(number)
            bytes = bytes.multiply(BigDecimal.valueOf(1024).pow(pow))
            returnValue = bytes.toLong()
        }
        return returnValue
    }
}