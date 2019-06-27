package com.cdsap.talaiot.publisher.json

import com.cdsap.talaiot.entities.TaskLength

data class JsonReport(
    val tasks: List<TaskLength>,
    val environment: Environment,
    val switches: Switches,
    val customProperties: CustomProperties,
    val plugins: List<Plugin>,
    val startMs: Long,
    val endMs: Long,
    val durationMs: Long
)


data class Environment(
    val cpuCount: Int,
    val osVersion: String,
    val maxWorkers: Int,
    val javaRuntime: String,
    val javaVmName: String,
    val javaXmsBytes: Long,
    val javaXmxBytes: Long,
    val totalRamAvailableBytes: Long,
    val locale: String,
    val username: String,
    val publicIp: String,
    val defaultChartset: String,
    val ideVersion: String,
    val gradleVersion: String,

    //Local/Remote
    val cacheMode: String,
    val cachePushEnabled: Boolean,
    val cacheUrl: String,
    val cacheHit: Long,
    val cacheMiss: Long,
    val cacheStore: Long
)

data class Switches(
    val buildCache: Boolean,
    val configurationOnDemand: Boolean,
    val daemon: Boolean,
    val parallel: Boolean,
    val continueOnFailure: Boolean,
    val dryRun: Boolean,
    val offline: Boolean,
    val rerunTasks: Boolean,
    val refreshDependencies: Boolean
)

data class CustomProperties(
    val properties: Map<String ,String>
)

data class Plugin(
    val id: String,
    val mainClass: String,
    val version: String
)