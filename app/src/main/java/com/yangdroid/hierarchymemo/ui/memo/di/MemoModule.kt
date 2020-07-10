package com.yangdroid.hierarchymemo.ui.memo.di

import com.yangdroid.hierarchymemo.di.ActivityScope
import com.yangdroid.hierarchymemo.model.domain.usecase.*
import com.yangdroid.hierarchymemo.ui.memo.MemoActivity
import com.yangdroid.hierarchymemo.ui.memo.MemoPresenter
import dagger.Module
import dagger.Provides

@Module
class MemoModule {

    @ActivityScope
    @Provides
    fun provideMemoPresenter(
        view: MemoActivity,
        getChildMemoList: GetChildMemoList,
        insertMemo: InsertMemo,
        deleteMemo: DeleteMemo,
        completeMemo: CompleteMemo,
        updateMemo: UpdateMemo
    ) = MemoPresenter(
        view,
        getChildMemoList,
        insertMemo,
        deleteMemo,
        completeMemo,
        updateMemo
    )

}