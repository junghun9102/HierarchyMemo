package com.yangdroid.hierarchymemo.ui.theme_setting

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.component.BasePresenter

class ThemeSettingPresenter(
    view: ThemeSettingContract.View
) : BasePresenter<ThemeSettingContract.View>(view), ThemeSettingContract.Presenter {

    var theme = ObservableField<Theme>()

    override fun onCreate(theme: Theme) {
        initSavedTheme(theme)
    }

    private fun initSavedTheme(theme: Theme) {
        this.theme.set(theme)
    }

    fun changeThemeToDefault() {
        theme.set(Theme.DEFAULT)
        view.updateConfigureToDefaultTheme()
    }

    fun changeThemeToLight() {
        theme.set(Theme.LIGHT)
        view.updateConfigureToLightTheme()
    }

    fun changeThemeToDark() {
        theme.set(Theme.DARK)
        view.updateConfigureToDarkTheme()
    }

    enum class Theme { DEFAULT, LIGHT, DARK }

}