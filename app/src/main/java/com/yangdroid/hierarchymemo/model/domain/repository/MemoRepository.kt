package com.yangdroid.hierarchymemo.model.domain.repository

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import io.reactivex.Completable
import io.reactivex.Single

interface MemoRepository {
    fun getMemo(id: Long): Single<Memo>
    fun getRootProgressMemoList(): Single<List<Memo>>
    fun getRootCompletedMemoList(): Single<List<Memo>>
    fun getChildMemoContentListByParentId(parentId: Long): Single<List<String>>
    fun insertMemo(memo: Memo): Single<Long>
    fun updateMemo(memo: Memo): Completable
    fun deleteMemo(memo: Memo): Completable
}