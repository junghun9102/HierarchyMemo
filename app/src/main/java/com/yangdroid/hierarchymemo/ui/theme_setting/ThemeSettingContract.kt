package com.yangdroid.hierarchymemo.ui.theme_setting

import com.yangdroid.hierarchymemo.component.BaseView

interface ThemeSettingContract {
    interface View : BaseView {
        fun updateConfigureToDefaultTheme()
        fun updateConfigureToLightTheme()
        fun updateConfigureToDarkTheme()
    }

    interface Presenter {
        fun onCreate(theme: ThemeSettingPresenter.Theme)
    }
}