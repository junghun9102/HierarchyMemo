package com.yangdroid.hierarchymemo.ui.main.di

import com.yangdroid.hierarchymemo.di.scope.ActivityScope
import com.yangdroid.hierarchymemo.model.domain.usecase.*
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
        getRootCompletedMemoList: GetRootCompletedMemoList
    ) = MainPresenter(
        view,
        getRootProgressMemoList,
        getRootCompletedMemoList
    )

}