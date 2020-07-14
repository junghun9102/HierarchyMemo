package com.yangdroid.hierarchymemo.di.module

import android.content.Context
import com.yangdroid.hierarchymemo.model.local.AppDatabase
import com.yangdroid.hierarchymemo.model.local.mapper.MemoMapper
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalModule {

    @JvmStatic
    @Provides
    fun provideAppDatabase(context: Context) = AppDatabase.getInstance(context)

    /**
     * Mapper
     */

    @JvmStatic
    @Singleton
    @Provides
    fun provideMemoMapper() = MemoMapper()

}