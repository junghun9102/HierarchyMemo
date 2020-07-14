package com.yangdroid.hierarchymemo.ui.memo.di

import com.yangdroid.hierarchymemo.di.scope.ActivityScope
import com.yangdroid.hierarchymemo.model.domain.usecase.*
import com.yangdroid.hierarchymemo.ui.memo.SubMemoActivity
import com.yangdroid.hierarchymemo.ui.memo.SubMemoPresenter
import dagger.Module
import dagger.Provides

@Module
class SubMemoModule {

    @ActivityScope
    @Provides
    fun provideMemoPresenter(
        view: SubMemoActivity,
        getChildMemoList: GetChildMemoList
    ) = SubMemoPresenter(
        view,
        getChildMemoList
    )

}