package com.cdsap.talaiot.publisher

/**
 * Extension Function for Type String to format tags and values
 * Influx Line Protocol and PushGateway requires specific format, we need to replace values like ","
 *
 * @return value formatted
 */

internal fun String.formatTagPublisher() = this.replace(Regex("""[ ,=,\,]"""), "")
