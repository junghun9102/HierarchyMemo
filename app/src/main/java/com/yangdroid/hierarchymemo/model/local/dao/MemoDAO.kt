package com.yangdroid.hierarchymemo.model.local.dao

import androidx.room.*
import com.yangdroid.hierarchymemo.model.local.dto.MemoDTO
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface MemoDAO {

    @Query("SELECT * FROM memo_table WHERE `id` == :id")
    fun getMemo(id: Long): Single<MemoDTO>

    @Query("SELECT * FROM memo_table WHERE parentId IS NULL AND completedDate IS NULL")
    fun getRootProgressMemoList(): Single<List<MemoDTO>>

    @Query("SELECT * FROM memo_table WHERE parentId IS NULL AND completedDate IS NOT NULL")
    fun getRootCompletedMemoList(): Single<List<MemoDTO>>

    @Query("SELECT * FROM memo_table WHERE parentId = :parentId")
    fun getChildMemoListByParentId(parentId: Long): Single<List<MemoDTO>>

    @Query("SELECT content FROM memo_table WHERE parentId = :parentId")
    fun getChildMemoContentListByParentId(parentId: Long): Single<List<String>>

    @Insert
    fun insert(memoDTO: MemoDTO): Single<Long>

    @Update
    fun update(memoDTO: MemoDTO): Completable

    @Delete
    fun delete(memoDTO: MemoDTO): Completable

}