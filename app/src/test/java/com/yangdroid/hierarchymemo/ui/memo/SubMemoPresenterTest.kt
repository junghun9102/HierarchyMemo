package com.yangdroid.hierarchymemo.ui.memo

import com.nhaarman.mockitokotlin2.argThat
import com.nhaarman.mockitokotlin2.check
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import com.yangdroid.hierarchymemo.model.domain.usecase.GetChildMemoList
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*
import kotlin.test.assertEquals

class SubMemoPresenterTest {

    @Mock private lateinit var view: SubMemoContract.View
    @Mock private lateinit var memoRepository: MemoRepository
    @Mock private lateinit var schedulersProvider: SchedulersProvider

    @InjectMocks private lateinit var getChildMemoList: GetChildMemoList

    private lateinit var presenter: SubMemoPresenter

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        presenter = SubMemoPresenter(view, getChildMemoList)

        `when`(schedulersProvider.io()).thenReturn(Schedulers.trampoline())
        `when`(schedulersProvider.ui()).thenReturn(Schedulers.trampoline())
    }

    @Test
    fun 서브_메모_초기화() {
        // Given
        val currentMemo = Memo(1, 0, "hello", emptyList(), Date(), null)
        val childMemoList = listOf(Memo(2, 1, "world", emptyList(), Date(), null))
        val childOfChildMemoContentList = listOf("A", "B", "C")
        `when`(memoRepository.getChildMemoListByParentId(ArgumentMatchers.anyLong()))
            .thenReturn(Single.just(childMemoList))
        `when`(memoRepository.getChildMemoContentListByParentId(ArgumentMatchers.anyLong()))
            .thenReturn(Single.just(childOfChildMemoContentList))

        // When
        presenter.onCreate(currentMemo)

        // Then
        verify(view).showMemoList(check {
            val childMemo = it.first()
            assertEquals(childMemo.id, 2)
            assertEquals(childMemo.content, "world")
            assertEquals(childMemo.childMemoContentList.size, 3)
        })
    }
}