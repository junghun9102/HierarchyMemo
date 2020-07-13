package com.yangdroid.hierarchymemo

import androidx.appcompat.app.AppCompatDelegate
import com.yangdroid.hierarchymemo.di.DaggerAppComponent
import com.yangdroid.hierarchymemo.model.local.sharedprefs.SharedPrefs
import com.yangdroid.hierarchymemo.ui.theme_setting.ThemeSettingPresenter
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        initSharedPrefs()
        initTheme()
    }

    private fun initSharedPrefs() {
        SharedPrefs.init(this)
    }

    private fun initTheme() {
        when (ThemeSettingPresenter.Theme.valueOf(SharedPrefs.getInstance().theme)) {
            ThemeSettingPresenter.Theme.DEFAULT -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            ThemeSettingPresenter.Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
            ThemeSettingPresenter.Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
        }.let {
            AppCompatDelegate.setDefaultNightMode(it)
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }
}