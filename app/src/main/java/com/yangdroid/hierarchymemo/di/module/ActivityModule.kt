package com.yangdroid.hierarchymemo.di.module

import com.yangdroid.hierarchymemo.di.scope.ActivityScope
import com.yangdroid.hierarchymemo.ui.main.MainActivity
import com.yangdroid.hierarchymemo.ui.main.di.MainModule
import com.yangdroid.hierarchymemo.ui.memo.SubMemoActivity
import com.yangdroid.hierarchymemo.ui.memo.di.SubMemoModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class, ViewModelModule::class])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [SubMemoModule::class, ViewModelModule::class])
    abstract fun memoActivity(): SubMemoActivity

}