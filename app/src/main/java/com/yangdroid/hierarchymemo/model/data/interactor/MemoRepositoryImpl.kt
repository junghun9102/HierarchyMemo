package com.yangdroid.hierarchymemo.model.data.interactor

import com.yangdroid.hierarchymemo.model.data.source.MemoLocalRepository
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import io.reactivex.Completable
import io.reactivex.Single

class MemoRepositoryImpl(
    private val local: MemoLocalRepository
) : MemoRepository {

    override fun getMemo(id: Long): Single<Memo> = local.getMemo(id)

    override fun getRootProgressMemoList(): Single<List<Memo>> = local.getRootProgressMemoList()

    override fun getRootCompletedMemoList(): Single<List<Memo>> = local.getRootCompletedMemoList()

    override fun getChildMemoContentListByParentId(parentId: Long): Single<List<String>> = local.getChildMemoContentListByParentId(parentId)

    override fun insertMemo(memo: Memo): Single<Long> = local.insertMemo(memo)

    override fun updateMemo(memo: Memo): Completable = local.updateMemo(memo)

    override fun deleteMemo(memo: Memo): Completable = local.deleteMemo(memo)

}