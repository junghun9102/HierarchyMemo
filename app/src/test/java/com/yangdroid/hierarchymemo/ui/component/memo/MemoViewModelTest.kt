package com.yangdroid.hierarchymemo.ui.component.memo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yangdroid.hierarchymemo.any
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.repository.MemoRepository
import com.yangdroid.hierarchymemo.model.domain.schedulers.SchedulersProvider
import com.yangdroid.hierarchymemo.model.domain.usecase.CompleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.DeleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.UpdateMemo
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.junit.Before

import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.util.*

class MemoViewModelTest {

    @Mock private lateinit var memoRepository: MemoRepository
    @Mock private lateinit var schedulersProvider: SchedulersProvider

    @InjectMocks private lateinit var insertMemo: InsertMemo
    @InjectMocks private lateinit var deleteMemo: DeleteMemo
    @InjectMocks private lateinit var completeMemo: CompleteMemo
    @InjectMocks private lateinit var updateMemo: UpdateMemo

    private lateinit var viewModel: MemoViewModel

    @JvmField
    @Rule
    val rule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        viewModel = MemoViewModel(insertMemo, deleteMemo, completeMemo, updateMemo)

        `when`(schedulersProvider.io()).thenReturn(Schedulers.trampoline())
        `when`(schedulersProvider.ui()).thenReturn(Schedulers.trampoline())
    }


    @Test
    fun 메모_모드_노말로_변경() {
        // When
        viewModel.input.changeModeToNormal()

        // Then
        assertEquals(viewModel.output.mode().get(), MemoViewModel.Mode.NORMAL)
    }

    @Test
    fun 메모_모드_생성으로_변경() {
        // Given
        var hasCalledFocusMemoEditTextAndShowKeyboard = false
        viewModel.output.focusMemoEditTextAndShowKeyboard().subscribe { hasCalledFocusMemoEditTextAndShowKeyboard = true }

        // When
        viewModel.input.changeModeToCreate()

        // Then
        assertEquals(viewModel.output.mode().get(), MemoViewModel.Mode.CREATE)
        assert(hasCalledFocusMemoEditTextAndShowKeyboard)
    }

    @Test
    fun 메모_모드_수정으로_변경() {
        // Given
        val memo = Memo(0, null, "Hello", emptyList(), Date(), null)
        var hasCalledFocusMemoEditTextAndShowKeyboard = false
        viewModel.output.focusMemoEditTextAndShowKeyboard().subscribe { hasCalledFocusMemoEditTextAndShowKeyboard = true }

        // When
        viewModel.input.changeModeToUpdate(memo)

        // Then
        assertEquals(viewModel.output.mode().get(), MemoViewModel.Mode.UPDATE)
        assert(hasCalledFocusMemoEditTextAndShowKeyboard)
    }

    @Test
    fun 메모_생성모드_작성버튼_클릭_아이디값이_없는경우() {
        // Given
        viewModel.input.changeModeToCreate()
        `when`(memoRepository.insertMemo(any(Memo::class.java))).thenReturn(Single.just(1L))

        var newMemo: Memo? = null
        viewModel.output.addNewMemoToRecyclerView().subscribe { newMemo = it }
        var hasCalledToastWriteSuccessMessage = false
        viewModel.output.toastWriteSuccessMessage().subscribe { hasCalledToastWriteSuccessMessage = true }
        var hasCalledHideSoftKeyboard = false
        viewModel.output.hideSoftKeyboard().subscribe { hasCalledHideSoftKeyboard = true }

        // When
        viewModel.input.clickWriteButton("hello")

        // Then
        assertEquals("hello", newMemo?.content)
        assertEquals(1L, newMemo?.id)
        assert(newMemo?.parentId == null)
        assert(hasCalledToastWriteSuccessMessage)
        assert(hasCalledHideSoftKeyboard)
    }

    @Test
    fun 메모_생성모드_작성버튼_클릭_아이디값이_있는경우() {
        // Given
        viewModel.input.changeModeToCreate()
        `when`(memoRepository.insertMemo(any(Memo::class.java))).thenReturn(Single.just(1L))

        var memo: Memo? = null
        viewModel.output.addNewMemoToRecyclerView().subscribe { memo = it }
        var hasCalledToastWriteSuccessMessage = false
        viewModel.output.toastWriteSuccessMessage().subscribe { hasCalledToastWriteSuccessMessage = true }
        var hasCalledHideSoftKeyboard = false
        viewModel.output.hideSoftKeyboard().subscribe { hasCalledHideSoftKeyboard = true }

        // When
        viewModel.input.clickWriteButton("hello", 2L)

        // Then
        assertEquals("hello", memo?.content)
        assertEquals(2L, memo?.parentId)
        assertEquals(1L, memo?.id)
        assert(hasCalledToastWriteSuccessMessage)
        assert(hasCalledHideSoftKeyboard)
    }

    @Test
    fun 메모_수정모드_작성버튼_클릭() {
        // Given
        val origin = Memo(0L, null, "안녕", emptyList(), Date(), null)
        viewModel.input.changeModeToUpdate(origin)
        `when`(memoRepository.updateMemo(any(Memo::class.java))).thenReturn(Completable.complete())

        var memo: Memo? = null
        viewModel.output.updateMemoToRecyclerView().subscribe { memo = it }
        var hasCalledToastUpdateSuccessMessage = false
        viewModel.output.toastUpdateSuccessMessage().subscribe { hasCalledToastUpdateSuccessMessage = true }
        var hasCalledHideSoftKeyboard = false
        viewModel.output.hideSoftKeyboard().subscribe { hasCalledHideSoftKeyboard = true }

        // When
        viewModel.input.clickWriteButton("hello")

        // Then
        assertEquals("hello", memo?.content)
        assertEquals(0L, memo?.id)
        assert(hasCalledToastUpdateSuccessMessage)
        assert(hasCalledHideSoftKeyboard)
    }

    @Test
    fun 메모_리사이클러뷰에서_삭제_시도_미완료목록() {
        // Given
        val progressMemo = Memo(0L, null, "안녕", emptyList(), Date(), null)
        `when`(memoRepository.updateMemo(any(Memo::class.java))).thenReturn(Completable.complete())

        var completedMemo: Memo? = null
        viewModel.output.addCompleteMemoToRecyclerView().subscribe { completedMemo = it }
        var hasCalledToastDeleteSuccessMessage = false
        viewModel.output.toastDeleteSuccessMessage().subscribe { hasCalledToastDeleteSuccessMessage = true }

        // When
        viewModel.input.onDeleteFromRecyclerView(progressMemo)

        // Then
        assert(completedMemo?.completedDate != null)
        assert(completedMemo!!.createdDate.before(completedMemo!!.completedDate!!))
        assert(hasCalledToastDeleteSuccessMessage)
    }

    @Test
    fun 메모_리사이클러뷰에서_삭제_시도_완료목록() {
        // Given
        val completedMemo = Memo(0L, null, "안녕", emptyList(), Date(), Date())
        `when`(memoRepository.deleteMemo(any(Memo::class.java))).thenReturn(Completable.complete())

        var hasCalledToastDeleteSuccessMessage = false
        viewModel.output.toastDeleteSuccessMessage().subscribe { hasCalledToastDeleteSuccessMessage = true }

        // When
        viewModel.input.onDeleteFromRecyclerView(completedMemo)

        // Then
        assert(hasCalledToastDeleteSuccessMessage)
    }

}