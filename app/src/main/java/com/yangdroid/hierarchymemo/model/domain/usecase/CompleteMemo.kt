package com.yangdroid.hierarchymemo.model.domain.usecase

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.interactor.SingleUseCase
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Single
import java.util.*
import javax.inject.Inject

class CompleteMemo @Inject constructor (
    private val memoRepository: MemoRepository,
    schedulersProvider: SchedulersProvider
) : SingleUseCase<Memo, Memo>(schedulersProvider) {

    override fun buildUseCaseSingle(params: Memo): Single<Memo> = memoRepository.updateMemo(
        params.apply { completedDate = Date() }
    ).toSingle { params }

}