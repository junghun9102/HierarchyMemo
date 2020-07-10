package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.ui.main.MainActivity
import com.yangdroid.hierarchymemo.ui.main.di.MainModule
import com.yangdroid.hierarchymemo.ui.memo.MemoActivity
import com.yangdroid.hierarchymemo.ui.memo.di.MemoModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun mainActivity(): MainActivity

    @ActivityScope
    @ContributesAndroidInjector(modules = [MemoModule::class])
    abstract fun memoActivity(): MemoActivity

}