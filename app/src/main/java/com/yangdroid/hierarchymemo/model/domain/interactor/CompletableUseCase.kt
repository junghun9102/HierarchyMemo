package com.yangdroid.hierarchymemo.model.domain.interactor

import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Completable

abstract class CompletableUseCase<in Params>(
    private val schedulersProvider: SchedulersProvider
) {
    protected abstract fun buildUseCaseCompletable(params: Params): Completable

    fun get(params: Params): Completable = buildUseCaseCompletable(params)
        .subscribeOn(schedulersProvider.io())
        .observeOn(schedulersProvider.ui())
}