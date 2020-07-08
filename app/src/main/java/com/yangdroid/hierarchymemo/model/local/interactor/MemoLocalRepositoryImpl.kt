package com.yangdroid.hierarchymemo.model.local.interactor

import com.yangdroid.hierarchymemo.model.data.source.MemoLocalRepository
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.local.AppDatabase
import com.yangdroid.hierarchymemo.model.local.mapper.MemoMapper
import io.reactivex.Completable
import io.reactivex.Single

class MemoLocalRepositoryImpl(
    private val database: AppDatabase,
    private val memoMapper: MemoMapper
) : MemoLocalRepository {

    override fun getMemo(id: Long): Single<Memo> = database.memoDAO()
        .getMemo(id)
        .map(memoMapper::mapToEntity)

    override fun getRootProgressMemoList(): Single<List<Memo>> = database.memoDAO()
        .getRootProgressMemoList()
        .map { list -> list.map(memoMapper::mapToEntity) }

    override fun getRootCompletedMemoList(): Single<List<Memo>> = database.memoDAO()
        .getRootCompletedMemoList()
        .map { list -> list.map(memoMapper::mapToEntity) }

    override fun getChildMemoContentListByParentId(parentId: Long): Single<List<String>> = database.memoDAO()
        .getChildMemoContentListByParentId(parentId)

    override fun insertMemo(memo: Memo): Single<Long> = database.memoDAO()
        .insert(memoMapper.mapToLocal(memo))

    override fun updateMemo(memo: Memo): Completable = database.memoDAO()
        .update(memoMapper.mapToLocal(memo))

    override fun deleteMemo(memo: Memo): Completable = database.memoDAO()
        .delete(memoMapper.mapToLocal(memo))

}