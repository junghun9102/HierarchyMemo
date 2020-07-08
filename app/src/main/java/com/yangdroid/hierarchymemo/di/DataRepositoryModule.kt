package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.model.data.source.MemoLocalRepository
import com.yangdroid.hierarchymemo.model.local.interactor.MemoLocalRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DataRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindRoutineLocal(memoLocalRepositoryImpl: MemoLocalRepositoryImpl): MemoLocalRepository

}