package com.agoda.gradle.tracking

data class TaskMeasurment(val task: String,
                          val ms: Long,
                          val user: String,
                          val branch: String,
                          val gradleVersion: String,
                          val totalMemory: Long,
                          val freeMemory: Long,
                          val maxMemory: Long,
                          val availableProcessors: Int)
