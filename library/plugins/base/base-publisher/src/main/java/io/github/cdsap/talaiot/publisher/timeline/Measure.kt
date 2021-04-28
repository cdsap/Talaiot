package io.github.cdsap.talaiot.publisher.timeline

import com.google.gson.annotations.SerializedName


data class Measure(@SerializedName("measure") val measure: String,
                   @SerializedName("data") val data: List<TimelineTaskMeasurement>
)
