package com.yangdroid.hierarchymemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import org.mockito.Mockito
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * For Mockito
 */

fun <T> any(type : Class<T>): T {
    Mockito.any(type)
    return uninitialized()
}

fun <T> anyList(type : Class<T>): List<T> {
    Mockito.any(type)
    return uninitialized()
}

@Suppress("UNCHECKED_CAST")
fun <T> uninitialized(): T = null as T

/**
 * For LiveData
 */

fun <T> LiveData<T>.getOrAwaitValue(
    time: Long = 2,
    timeUnit: TimeUnit = TimeUnit.SECONDS
): T {
    var data: T? = null
    val latch = CountDownLatch(1)
    val observer = object : Observer<T> {
        override fun onChanged(o: T?) {
            data = o
            latch.countDown()
            this@getOrAwaitValue.removeObserver(this)
        }
    }

    this.observeForever(observer)

    // Don't wait indefinitely if the LiveData is not set.
    if (!latch.await(time, timeUnit)) {
        throw TimeoutException("LiveData value was never set.")
    }

    @Suppress("UNCHECKED_CAST")
    return data as T
}
