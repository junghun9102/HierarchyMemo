package com.yangdroid.hierarchymemo.ui.main.di

import com.yangdroid.hierarchymemo.di.ActivityScope
import com.yangdroid.hierarchymemo.ui.main.MainActivity
import com.yangdroid.hierarchymemo.ui.main.MainContract
import com.yangdroid.hierarchymemo.ui.main.MainPresenter
import dagger.Module
import dagger.Provides

@Module
class MainModule {

    @ActivityScope
    @Provides
    fun provideMainPresenter(view: MainActivity) = MainPresenter(view)

}