package com.yangdroid.hierarchymemo.ui.main.di

import com.yangdroid.hierarchymemo.di.ActivityScope
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootCompletedMemoList
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootProgressMemoList
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import com.yangdroid.hierarchymemo.ui.main.MainActivity
import com.yangdroid.hierarchymemo.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideMainPresenter(
        view: MainActivity,
        getRootProgressMemoList: GetRootProgressMemoList,
        getRootCompletedMemoList: GetRootCompletedMemoList,
        insertMemo: InsertMemo
    ) = MainPresenter(
        view,
        getRootProgressMemoList,
        getRootCompletedMemoList,
        insertMemo
    )

}