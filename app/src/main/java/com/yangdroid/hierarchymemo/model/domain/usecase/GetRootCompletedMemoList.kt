package com.yangdroid.hierarchymemo.model.domain.usecase

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.interactor.SingleWithoutInputUseCase
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetRootCompletedMemoList @Inject constructor (
    private val memoRepository: MemoRepository,
    schedulersProvider: SchedulersProvider
) : SingleWithoutInputUseCase<List<Memo>>(schedulersProvider) {

    override fun buildUseCaseSingle(): Single<List<Memo>> = memoRepository.getRootCompletedMemoList()
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