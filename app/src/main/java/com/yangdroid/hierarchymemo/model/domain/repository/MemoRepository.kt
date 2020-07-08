package com.yangdroid.hierarchymemo.model.domain.repository

import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import io.reactivex.Completable
import io.reactivex.Single

interface MemoRepository {
    fun insertMemo(memo: Memo): Single<Long>
    fun updateMemo(memo: Memo): Completable
    fun deleteeMemo(memo: Memo): Completable
    fun getMemo(id: Long): Single<Memo>
    fun getRootProgressMemoList(): Single<List<Memo>>
    fun getRootCompletedMemoList(): Single<List<Memo>>
}