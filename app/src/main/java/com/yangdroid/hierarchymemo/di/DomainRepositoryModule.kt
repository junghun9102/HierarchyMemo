package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.AppSchedulersProvider
import com.yangdroid.hierarchymemo.model.data.interactor.MemoRepositoryImpl
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMemoRepository(memoRepositoryImpl: MemoRepositoryImpl): MemoRepository

    /**
     * Schedulers Provider
     */

    @Singleton
    @Binds
    abstract fun bindSchedulersProvider(appSchedulersProvider: AppSchedulersProvider): SchedulersProvider

}