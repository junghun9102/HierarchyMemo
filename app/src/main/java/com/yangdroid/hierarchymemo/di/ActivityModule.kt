package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.ui.main.MainActivity
import com.yangdroid.hierarchymemo.ui.main.di.MainModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainModule::class])
    abstract fun mainActivity(): MainActivity

}