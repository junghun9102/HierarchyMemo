package com.yangdroid.hierarchymemo.model.domain.schedulers

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler
    fun ui(): Scheduler
}