package com.yangdroid.hierarchymemo.model.domain.interactor

import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Completable

abstract class CompletableWithoutInputUseCase(
    private val schedulersProvider: SchedulersProvider
) {
    protected abstract fun buildUseCaseCompletable(): Completable

    fun get(): Completable = buildUseCaseCompletable()
        .subscribeOn(schedulersProvider.io())
        .observeOn(schedulersProvider.ui())
}