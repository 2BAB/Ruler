package me.xx2bab.ruler.demo

import android.app.Activity
import android.os.Bundle
import me.xx2bab.ruler.base.sampler.cpu.CPUSampler

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        CPUSampler().sample()
    }
}