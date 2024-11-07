package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.internal.provider.Providers

class KotlinProcessMetricsTest : BehaviorSpec({
    given("a Kotlin Process Metrics object") {
        `when`("There are more than one Kotlin Process") {
            val provider = Providers.of(
                """
                -XX:CICompilerCount=4 -XX:CompressedClassSpaceSize=859832320 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1EagerReclaimRemSetThreshold=16 -XX:G1HeapRegionSize=2097152 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=3221225472 -XX:MaxMetaspaceSize=1073741824 -XX:MaxNewSize=1931476992 -XX:MinHeapDeltaBytes=2097152 -XX:MinHeapSize=8388608 -XX:NonNMethodCodeHeapSize=5839564 -XX:NonProfiledCodeHeapSize=122909338 -XX:ProfiledCodeHeapSize=122909338 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:SoftMaxHeapSize=3221225472 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseNUMA -XX:-UseNUMAInterleaving
                41922
                -XX:CICompilerCount=4 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1HeapRegionSize=1048576 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=4294967296 -XX:MaxNewSize=2576351232 -XX:MinHeapDeltaBytes=1048576 -XX:NonNMethodCodeHeapSize=5836492 -XX:NonProfiledCodeHeapSize=122910874 -XX:ProfiledCodeHeapSize=122910874 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:-UseAOT -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC
                3442
                -XX:CICompilerCount=4 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1EagerReclaimRemSetThreshold=8 -XX:G1HeapRegionSize=1048576 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=2147483648 -XX:MaxNewSize=1287651328 -XX:MinHeapDeltaBytes=1048576 -XX:MinHeapSize=8388608 -XX:NonNMethodCodeHeapSize=5839564 -XX:NonProfiledCodeHeapSize=122909338 -XX:ProfiledCodeHeapSize=122909338 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:SoftMaxHeapSize=2147483648 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseNUMA -XX:-UseNUMAInterleaving
                34905
                """.trimIndent()
            )

            val executionReport = ExecutionReport()
            KotlinProcessMetrics(provider.get()).get(Unit, executionReport)
            then("Execution Report sets multipleKotlinProcesses and parse correctly 2 different processes") {
                val env = executionReport.environment
                assert(env.multipleKotlinProcesses == true)
                assert(env.kotlinProcessesAvailable == 3)
                assert(env.multipleKotlinJvmArgs?.get("41922")?.get("-XX:CICompilerCount") == "4")
                assert(env.multipleKotlinJvmArgs?.get("41922")?.get("-XX:G1HeapRegionSize") == "2097152")
                assert(env.multipleKotlinJvmArgs?.get("3442")?.get("-XX:G1ConcRefinementThreads") == "9")
                assert(env.multipleKotlinJvmArgs?.get("3442")?.get("-XX:G1HeapRegionSize") == "1048576")
                assert(env.multipleKotlinJvmArgs?.get("34905")?.get("-XX:G1EagerReclaimRemSetThreshold") == "8")
                assert(env.multipleKotlinJvmArgs?.get("34905")?.get("-XX:G1HeapRegionSize") == "1048576")
            }
        }
        `when`("There is only one Kotlin Process") {
            val provider = Providers.of(
                """
                -XX:CICompilerCount=4 -XX:CompressedClassSpaceSize=859832320 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1EagerReclaimRemSetThreshold=16 -XX:G1HeapRegionSize=2097152 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=3221225472 -XX:MaxMetaspaceSize=1073741824 -XX:MaxNewSize=1931476992 -XX:MinHeapDeltaBytes=2097152 -XX:MinHeapSize=8388608 -XX:NonNMethodCodeHeapSize=5839564 -XX:NonProfiledCodeHeapSize=122909338 -XX:ProfiledCodeHeapSize=122909338 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:SoftMaxHeapSize=3221225472 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC -XX:-UseNUMA -XX:-UseNUMAInterleaving
                41922
                """.trimIndent()
            )

            val executionReport = ExecutionReport()
            KotlinProcessMetrics(provider.get()).get(Unit, executionReport)
            then("Execution Report sets multipleKotlinProcesses and parse correctly 2 different processes") {
                assert(executionReport.environment.multipleKotlinProcesses == false)
                assert(executionReport.environment.kotlinProcessesAvailable == 1)
                assert(executionReport.environment.kotlinJvmArgs?.get("-XX:CICompilerCount") == "4")
                assert(executionReport.environment.kotlinJvmArgs?.get("-XX:CompressedClassSpaceSize") == "859832320")
                assert(executionReport.environment.kotlinJvmArgs?.get("-XX:+UseG1GC") == "true")
            }
        }
        `when`("There is no Kotlin Process") {
            val provider = Providers.of(
                """
                """.trimIndent()
            )

            val executionReport = ExecutionReport()
            KotlinProcessMetrics(provider.get()).get(Unit, executionReport)
            then("Values are not set in Execution Report") {
                assert(executionReport.environment.multipleKotlinProcesses == null)
                assert(executionReport.environment.kotlinProcessesAvailable == null)
            }
        }
    }
})
