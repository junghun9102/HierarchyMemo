package com.yangdroid.hierarchymemo.model.domain.usecase

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.interactor.CompletableUseCase
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Completable

class UpdateMemo (
    private val memoRepository: MemoRepository,
    schedulersProvider: SchedulersProvider
) : CompletableUseCase<Memo>(schedulersProvider) {

    override fun buildUseCaseCompletable(params: Memo): Completable = memoRepository.updateMemo(params)

}