package com.yangdroid.hierarchymemo.di

import com.yangdroid.hierarchymemo.model.data.interactor.MemoRepositoryImpl
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class DomainRepositoryModule {

    @Singleton
    @Binds
    abstract fun bindMemoRepository(memoRepositoryImpl: MemoRepositoryImpl): MemoRepository

}