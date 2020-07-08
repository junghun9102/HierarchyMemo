package com.yangdroid.hierarchymemo.model.domain.usecase

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.interactor.SingleUseCase
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Single
import javax.inject.Inject

class InsertMemo @Inject constructor (
    private val memoRepository: MemoRepository,
    schedulersProvider: SchedulersProvider
) : SingleUseCase<Long, Memo>(schedulersProvider) {

    override fun buildUseCaseSingle(params: Memo): Single<Long> = memoRepository.insertMemo(params)

}