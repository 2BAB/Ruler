package me.xx2bab.ruler.base.sampler.cpu

import android.os.Process
import android.util.Log
import me.xx2bab.ruler.base.sampler.common.Sampler
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.InputStreamReader

class CPUSampler: Sampler {

    override fun sample() {
        val buffer = 1024
        val pidReader = BufferedReader(InputStreamReader(
                FileInputStream("/proc/" + Process.myPid().toString() + "/stat")), buffer)
        val pidCpuRate = pidReader.readLine()
        Log.d("CPUSampler", " $pidCpuRate")
    }

}