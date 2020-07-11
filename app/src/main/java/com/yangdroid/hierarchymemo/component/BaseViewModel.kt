package com.yangdroid.hierarchymemo.component

import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel {

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    fun onCleared() {
        compositeDisposable.clear()
    }

}