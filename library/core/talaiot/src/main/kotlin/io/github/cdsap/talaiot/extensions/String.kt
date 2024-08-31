package io.github.cdsap.talaiot.extensions

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

fun String.gradleVersionCompatibleWithIsolatedProjects(): Boolean {
    fun parseVersion(version: String): List<Int> {
        return version.split(Regex("[^\\d]+")).filter { it.isNotEmpty() }.map { it.toInt() }
    }

    val versionParts = parseVersion(this)
    val targetVersionParts = parseVersion("8.5")
    val maxLength = maxOf(versionParts.size, targetVersionParts.size)
    for (i in 0 until maxLength) {
        val vPart = versionParts.getOrElse(i) { 0 }

        val tPart = targetVersionParts.getOrElse(i) { 0 }
        if (vPart > tPart) {
            return true
        }
        if (vPart < tPart) {
            return false
        }
    }
    return true
}
