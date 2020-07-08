package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.any
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class MainPresenterTest {

    @Mock
    private lateinit var view: MainContract.View

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view)
    }

    @Test
    fun 메인_초기화시_날짜_초기화() {
        // When
        presenter.onCreate()
        // Then
        verify(view).showTodayDate(any(Date::class.java))
    }

}