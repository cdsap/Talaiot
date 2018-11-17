package com.agoda.gradle.tracking

interface ReportingStrategy {
    fun send(measurementAggregated: TaskMeasurementAggregated)
}