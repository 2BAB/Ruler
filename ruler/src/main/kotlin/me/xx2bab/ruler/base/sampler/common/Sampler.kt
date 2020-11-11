package me.xx2bab.ruler.base.sampler.common

import kotlinx.coroutines.flow.Flow
import java.util.concurrent.atomic.AtomicBoolean

interface Sampler<T> {

    fun sample(interval: Long, workingFlag: AtomicBoolean): Flow<T>

}