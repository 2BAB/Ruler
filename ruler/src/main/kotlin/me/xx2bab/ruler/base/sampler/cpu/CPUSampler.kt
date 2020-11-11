package me.xx2bab.ruler.base.sampler.cpu

import android.os.Process
import android.util.Log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.xx2bab.ruler.base.sampler.common.Sampler
import java.io.File
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

class CPUSampler : Sampler<CPUSample> {

    private var latestProcessCPUTime = 0L

    override fun sample(interval: Long,
                        workingFlag: AtomicBoolean): Flow<CPUSample> {
        return flow {
            while (workingFlag.get()) {
                val deviceCPUSample = deviceCPUSample()
                val processCPUSample = processCPUSample()
                val utilization = CPUSample(
                        deviceCPUSample.first,
                        processCPUSample,
                        deviceCPUSample.second
                )
                emit(utilization)
                delay(interval)
            }
        }
    }

    private fun processCPUSample(): String {
        val rawUsage = try {
            File("/proc/" + Process.myPid().toString() + "/stat").readText()
        } catch (e: Exception) {
            Log.e(CPUSampler::class.simpleName, "Permission denied or parsing exception" +
                    " when reading /proc/\${pid}/stat." +
                    " Please check error messages below:\n ${e.message}.")
            ""
        }
        val rawUsageList = rawUsage.split(" ")
        if (rawUsageList.size < 17) {
            return ""
        }

        val cpuTime = (rawUsageList[13].toLong()
                + rawUsageList[14].toLong()
                + rawUsageList[15].toLong()
                + rawUsageList[16].toLong())

        val res = (cpuTime - latestProcessCPUTime).toString()
        latestProcessCPUTime = cpuTime
        return res
    }

    private fun deviceCPUSample(): Pair<String, List<CPUInfo>> {
        val procCounts = Runtime.getRuntime().availableProcessors()
        var averageUtilization = 0.0
        val list = LinkedList<CPUInfo>()
        for (i in 0 until procCounts) {
            if (!File("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq").exists()) {
                continue
            }
            val curr = File("/sys/devices/system/cpu/cpu$i/cpufreq/scaling_cur_freq").readText().trim().toInt()
            val max = File("/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_max_freq").readText().trim().toInt()
            val min = File("/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_min_freq").readText().trim().toInt()
            list.addLast(CPUInfo(curr.toString(),
                    max.toString(),
                    min.toString(),
                    ((curr - min) * 1.0 / max).toString()
            ))
            averageUtilization += curr * 1.0 / max
        }
        averageUtilization /= procCounts
        return Pair(averageUtilization.toString(), list)
    }

}