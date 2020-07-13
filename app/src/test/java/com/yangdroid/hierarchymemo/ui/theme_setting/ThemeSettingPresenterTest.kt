package com.yangdroid.hierarchymemo.ui.theme_setting

import com.nhaarman.mockitokotlin2.verify
import org.junit.Before

import org.junit.Assert.*
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class ThemeSettingPresenterTest {

    @Mock private lateinit var view: ThemeSettingContract.View

    private lateinit var presenter: ThemeSettingPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = ThemeSettingPresenter(view)
    }

    @Test
    fun 테마설정_초기화() {
        // Given
        val savedTheme = ThemeSettingPresenter.Theme.DARK

        // When
        presenter.onCreate(savedTheme)

        // Then
        assertEquals(ThemeSettingPresenter.Theme.DARK, presenter.theme.get())
    }

    @Test
    fun 테마설정_디폴트로_설정() {
        // Given
        presenter.onCreate(ThemeSettingPresenter.Theme.DARK)

        // When
        presenter.changeThemeToDefault()

        // Then
        assertEquals(ThemeSettingPresenter.Theme.DEFAULT, presenter.theme.get())
        verify(view).updateConfigureToDefaultTheme()
    }

    @Test
    fun 테마설정_라이트로_설정() {
        // Given
        presenter.onCreate(ThemeSettingPresenter.Theme.DARK)

        // When
        presenter.changeThemeToLight()

        // Then
        assertEquals(ThemeSettingPresenter.Theme.LIGHT, presenter.theme.get())
        verify(view).updateConfigureToLightTheme()
    }

    @Test
    fun 테마설정_다크로_설정() {

        // Given
        presenter.onCreate(ThemeSettingPresenter.Theme.DEFAULT)

        // When
        presenter.changeThemeToDark()

        // Then
        assertEquals(ThemeSettingPresenter.Theme.DARK, presenter.theme.get())
        verify(view).updateConfigureToDarkTheme()
    }
}