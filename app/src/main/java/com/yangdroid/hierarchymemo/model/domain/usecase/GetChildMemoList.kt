package com.yangdroid.hierarchymemo.model.domain.usecase

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.interactor.SingleUseCase
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Observable
import io.reactivex.Single

class GetChildMemoList (
    private val memoRepository: MemoRepository,
    schedulersProvider: SchedulersProvider
) : SingleUseCase<List<Memo>, Long>(schedulersProvider) {

    override fun buildUseCaseSingle(params: Long): Single<List<Memo>> = memoRepository.getChildMemoListByParentId(params)
        .flatMapObservable { memoList -> Observable.fromIterable(memoList) }
        .flatMapSingle { memo ->
            memoRepository.getChildMemoContentListByParentId(memo.id!!)
                .map { childContentList ->
                    memo.apply {
                        childMemoContentList = childContentList
                    }
                }
        }.toList()

}