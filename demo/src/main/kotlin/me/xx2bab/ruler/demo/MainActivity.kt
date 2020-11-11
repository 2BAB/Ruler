package me.xx2bab.ruler.demo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.xx2bab.ruler.base.sampler.cpu.CPUSampler
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val interval = 80L
        val workingFlag = AtomicBoolean(true)
        val flow1 = CPUSampler().sample(interval, workingFlag)
        lifecycleScope.launch(Dispatchers.IO) {
            flow1.collect {
                Log.d("MainActivity", it.toString())
            }
        }

        Handler().postDelayed({
            workingFlag.set(false)
        }, 3000)
    }
}