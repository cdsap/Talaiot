package com.cdsap.talaiot.publisher.base.timeline

import com.google.gson.annotations.SerializedName


data class Measure(@SerializedName("measure") val measure: String,
                   @SerializedName("data") val data: List<TimelineTaskMeasurement>
)
