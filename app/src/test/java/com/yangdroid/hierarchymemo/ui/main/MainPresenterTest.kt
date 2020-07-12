package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.any
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootCompletedMemoList
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootProgressMemoList
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class MainPresenterTest {

    @Mock private lateinit var view: MainContract.View
    @Mock private lateinit var memoRepository: MemoRepository
    @Mock private lateinit var schedulersProvider: SchedulersProvider

    @InjectMocks private lateinit var getRootProgressMemoList: GetRootProgressMemoList
    @InjectMocks private lateinit var getRootCompletedMemoList: GetRootCompletedMemoList

    private lateinit var presenter: MainPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = MainPresenter(view, getRootProgressMemoList, getRootCompletedMemoList)

        `when`(schedulersProvider.io()).thenReturn(Schedulers.trampoline())
        `when`(schedulersProvider.ui()).thenReturn(Schedulers.trampoline())
    }

    @Test
    fun 메인_초기화() {
        // Given
        val list = listOf(Memo(0L, null, "hello", emptyList(), Date(), null))
        val childMemoContentList = listOf("A", "B", "C")
        `when`(memoRepository.getRootProgressMemoList()).thenReturn(Single.just(list))
        `when`(memoRepository.getChildMemoContentListByParentId(ArgumentMatchers.anyLong())).thenReturn(Single.just(childMemoContentList))

        // When
        presenter.onCreate()

        // Then
        verify(view).showTodayDate(any(Date::class.java))
        verify(view).showMemoList(list)
    }

    @Test
    fun 메인_메모_타입_변경_프로그래스() {
        // Given
        val list = listOf(Memo(0L, null, "hello", emptyList(), Date(), null))
        val childMemoContentList = listOf("A", "B", "C", "D")
        `when`(memoRepository.getRootProgressMemoList()).thenReturn(Single.just(list))
        `when`(memoRepository.getChildMemoContentListByParentId(ArgumentMatchers.anyLong())).thenReturn(Single.just(childMemoContentList))

        // When
        presenter.changeTypeToProgress()

        // Then
        verify(view).showMemoList(list)
        assertEquals(MainPresenter.MemoTypeToLoad.PROGRESS, presenter.type.get())
    }

    @Test
    fun 메인_메모_타입_변경_완료() {
        // Given
        val list = listOf(Memo(0L, null, "hello", emptyList(), Date(), null))
        val childMemoContentList = listOf("A", "B", "C", "D", "E")
        `when`(memoRepository.getRootCompletedMemoList()).thenReturn(Single.just(list))
        `when`(memoRepository.getChildMemoContentListByParentId(ArgumentMatchers.anyLong())).thenReturn(Single.just(childMemoContentList))

        // When
        presenter.changeTypeToCompleted()

        // Then
        verify(view).showMemoList(list)
        assertEquals(MainPresenter.MemoTypeToLoad.COMPLETED, presenter.type.get())
    }
}