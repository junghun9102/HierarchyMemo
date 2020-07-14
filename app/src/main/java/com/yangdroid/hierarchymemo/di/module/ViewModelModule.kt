package com.yangdroid.hierarchymemo.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yangdroid.hierarchymemo.component.ViewModelFactory
import com.yangdroid.hierarchymemo.di.scope.ViewModelKey
import com.yangdroid.hierarchymemo.model.domain.usecase.CompleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.DeleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.UpdateMemo
import com.yangdroid.hierarchymemo.ui.component.memo.MemoViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    abstract fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MemoViewModel::class)
    abstract fun provideMemoViewModel(memoViewModel: MemoViewModel): ViewModel

}