package com.yangdroid.hierarchymemo.model.domain.interactor

import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Single

abstract class SingleWithoutInputUseCase<T>(
    private val schedulersProvider: SchedulersProvider
) {
    protected  abstract fun buildUseCaseSingle(): Single<T>

    fun get() = buildUseCaseSingle()
        .subscribeOn(schedulersProvider.io())
        .observeOn(schedulersProvider.ui())
}