package io.github.cdsap.talaiot.metrics

import io.github.cdsap.talaiot.entities.ExecutionReport
import io.kotlintest.specs.BehaviorSpec
import org.gradle.api.internal.provider.Providers

class GradleProcessMetricsTest : BehaviorSpec({
    given("a Gradle Process Metrics object") {
        `when`("There are more than one Gradle Process") {
            val provider = Providers.of(
                """
                    -XX:CICompilerCount=4 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1HeapRegionSize=1048576 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=4294967296 -XX:MaxNewSize=2576351232 -XX:MinHeapDeltaBytes=1048576 -XX:NonNMethodCodeHeapSize=5836492 -XX:NonProfiledCodeHeapSize=122910874 -XX:ProfiledCodeHeapSize=122910874 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:-UseAOT -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC
                    8848
                    -XX:CICompilerCount=4 -XX:CompressedClassSpaceSize=859832320 -XX:+HeapDumpOnOutOfMemoryError -XX:InitialHeapSize=1073741824 -XX:MaxHeapSize=3221225472 -XX:MaxMetaspaceSize=1073741824 -XX:MaxNewSize=1073741824 -XX:MinHeapDeltaBytes=524288 -XX:MinHeapSize=8388608 -XX:NewSize=357564416 -XX:NonNMethodCodeHeapSize=5839564 -XX:NonProfiledCodeHeapSize=122909338 -XX:OldSize=716177408 -XX:ProfiledCodeHeapSize=122909338 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:SoftMaxHeapSize=3221225472 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:-UseNUMA -XX:-UseNUMAInterleaving -XX:+UseParallelGC
                    9105
                """.trimIndent()
            )

            val executionReport = ExecutionReport()
            GradleProcessMetrics(provider).get(Unit, executionReport)
            then("Execution Report sets multipleGradleProcesses and parse correctly 2 different processes") {
                assert(executionReport.environment.multipleGradleProcesses == true)
                assert(executionReport.environment.gradleProcessesAvailable == 2)
                assert(
                    executionReport.environment.multipleGradleJvmArgs?.get("8848")?.get("-XX:CICompilerCount") == "4"
                )
                assert(
                    executionReport.environment.multipleGradleJvmArgs?.get("8848")
                        ?.get("-XX:G1HeapRegionSize") == "1048576"
                )
                assert(
                    executionReport.environment.multipleGradleJvmArgs?.get("9105")
                        ?.get("-XX:CompressedClassSpaceSize") == "859832320"
                )
                assert(
                    executionReport.environment.multipleGradleJvmArgs?.get("9105")
                        ?.get("-XX:+HeapDumpOnOutOfMemoryError") == "true"
                )
            }
        }
        `when`("There is only one Gradle Process") {
            val provider = Providers.of(
                """
                    -XX:CICompilerCount=4 -XX:ConcGCThreads=2 -XX:G1ConcRefinementThreads=9 -XX:G1HeapRegionSize=1048576 -XX:GCDrainStackTargetSize=64 -XX:InitialHeapSize=1073741824 -XX:MarkStackSize=4194304 -XX:MaxHeapSize=4294967296 -XX:MaxNewSize=2576351232 -XX:MinHeapDeltaBytes=1048576 -XX:NonNMethodCodeHeapSize=5836492 -XX:NonProfiledCodeHeapSize=122910874 -XX:ProfiledCodeHeapSize=122910874 -XX:ReservedCodeCacheSize=251658240 -XX:+SegmentedCodeCache -XX:-UseAOT -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseG1GC
                    8848
                """.trimIndent()
            )

            val executionReport = ExecutionReport()
            GradleProcessMetrics(provider).get(Unit, executionReport)
            then("Execution Report sets multipleGradleProcesses and parse correctly 2 different processes") {
                assert(executionReport.environment.multipleGradleProcesses == false)
                assert(executionReport.environment.gradleProcessesAvailable == 1)
                assert(executionReport.environment.gradleJvmArgs?.get("-XX:CICompilerCount") == "4")
                assert(executionReport.environment.gradleJvmArgs?.get("-XX:ConcGCThreads") == "2")
                assert(executionReport.environment.gradleJvmArgs?.get("-XX:+UseG1GC") == "true")
            }
        }
    }
})
