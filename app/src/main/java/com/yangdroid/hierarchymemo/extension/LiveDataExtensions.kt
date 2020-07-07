package com.yangdroid.hierarchymemo.extension

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<Unit>.call(ignore: T) {
    value = Unit
}

fun MutableLiveData<Unit>.call() {
    value = Unit
}

inline fun <T> LiveData<T>.observe(owner: LifecycleOwner, crossinline observer: (T?) -> Unit) {
    this.observe(owner, androidx.lifecycle.Observer { observer(it) })
}

inline fun <T> LiveData<T>.observeNotNull(
    owner: LifecycleOwner,
    crossinline observer: (T) -> Unit
) {
    this.observe(owner, androidx.lifecycle.Observer { observer(it!!) })
}