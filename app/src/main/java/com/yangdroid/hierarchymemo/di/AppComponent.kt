package com.yangdroid.hierarchymemo.di

import android.app.Application
import com.yangdroid.hierarchymemo.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules =[
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    DomainRepositoryModule::class,
    DataRepositoryModule::class,
    LocalModule::class,
    ComponentModule::class
])
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

}