package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.model.domain.usecase.CompleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.DeleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.UpdateMemo
import com.yangdroid.hierarchymemo.ui.component.memo.MemoViewModel
import dagger.Module
import dagger.Provides

@Module
object ComponentModule {

    @JvmStatic
    @Provides
    fun provideMemoViewModel(
        insertMemo: InsertMemo,
        deleteMemo: DeleteMemo,
        completeMemo: CompleteMemo,
        updateMemo: UpdateMemo
    ) = MemoViewModel(
        insertMemo, deleteMemo, completeMemo, updateMemo
    )

}