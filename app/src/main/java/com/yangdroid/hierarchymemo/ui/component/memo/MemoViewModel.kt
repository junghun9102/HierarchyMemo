package com.yangdroid.hierarchymemo.ui.component.memo

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.component.BaseViewModel
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.usecase.CompleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.DeleteMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import com.yangdroid.hierarchymemo.model.domain.usecase.UpdateMemo
import io.reactivex.subjects.PublishSubject
import java.util.*

class MemoViewModel(
    private val insertMemo: InsertMemo,
    private val deleteMemo: DeleteMemo,
    private val completeMemo: CompleteMemo,
    private val updateMemo: UpdateMemo
) : BaseViewModel() {

    private val mode = ObservableField<Mode>(Mode.NORMAL)
    private var memoToUpdate: Memo? = null

    private val changeMode = PublishSubject.create<Mode>()
    private val clickWriteButton = PublishSubject.create<Pair<String, Long?>>()
    private val onDeleteFromRecyclerView = PublishSubject.create<Memo>()
    val input: MemoViewModelInputs = object : MemoViewModelInputs {
        override fun changeModeToNormal() = changeMode.onNext(Mode.NORMAL)
        override fun changeModeToCreate() = changeMode.onNext(Mode.CREATE)
        override fun changeModeToUpdate(memoToUpdate: Memo) {
            this@MemoViewModel.memoToUpdate = memoToUpdate
            changeMode.onNext(Mode.UPDATE)
        }
        override fun clickWriteButton(content: String, currentMemoId: Long?) = clickWriteButton.onNext(Pair(content, currentMemoId))
        override fun onDeleteFromRecyclerView(memo: Memo) = onDeleteFromRecyclerView.onNext(memo)
    }

    private val toastErrorMessage = PublishSubject.create<String>()
    private val toastDeleteSuccessMessage = PublishSubject.create<Unit>()
    private val toastDeleteFailMessage = PublishSubject.create<Unit>()
    private val toastWriteSuccessMessage = PublishSubject.create<Unit>()
    private val toastUpdateSuccessMessage = PublishSubject.create<Unit>()
    private val hideSoftKeyboard = PublishSubject.create<Unit>()
    private val focusMemoEditTextAndShowKeyboard = PublishSubject.create<Unit>()
    private val addNewMemoToRecyclerView = PublishSubject.create<Memo>()
    private val addCompleteMemoToRecyclerView = PublishSubject.create<Memo>()
    private val updateMemoToRecyclerView = PublishSubject.create<Memo>()
    val output: MemoViewModelOutputs = object : MemoViewModelOutputs {
        override fun mode(): ObservableField<Mode> = mode
        override fun toastErrorMessage(): PublishSubject<String> = toastErrorMessage
        override fun toastDeleteSuccessMessage(): PublishSubject<Unit> = toastDeleteSuccessMessage
        override fun toastDeleteFailMessage(): PublishSubject<Unit> = toastDeleteFailMessage
        override fun toastWriteSuccessMessage(): PublishSubject<Unit> = toastWriteSuccessMessage
        override fun toastUpdateSuccessMessage(): PublishSubject<Unit> = toastUpdateSuccessMessage
        override fun hideSoftKeyboard(): PublishSubject<Unit> = hideSoftKeyboard
        override fun focusMemoEditTextAndShowKeyboard(): PublishSubject<Unit> = focusMemoEditTextAndShowKeyboard
        override fun addNewMemoToRecyclerView(): PublishSubject<Memo> = addNewMemoToRecyclerView
        override fun addCompleteMemoToRecyclerView(): PublishSubject<Memo> = addCompleteMemoToRecyclerView
        override fun updateMemoToRecyclerView(): PublishSubject<Memo> = updateMemoToRecyclerView
    }

    init {
        compositeDisposable += changeMode.subscribe(::changeMode)
        compositeDisposable += clickWriteButton.subscribe { clickWriteButton(it.first, it.second) }
        compositeDisposable += onDeleteFromRecyclerView.subscribe(::onDeleteFromRecyclerView)
    }

    private fun changeMode(mode: Mode) {
        when (mode) {
            Mode.NORMAL -> changeModeToNormal()
            Mode.CREATE -> changeModeToCreate()
            Mode.UPDATE -> changeModeToUpdate()
        }
    }

    private fun changeModeToNormal() {
        mode.set(Mode.NORMAL)
        memoToUpdate = null
    }

    private fun changeModeToCreate() {
        mode.set(Mode.CREATE)
        focusMemoEditTextAndShowKeyboard.call()
    }

    private fun changeModeToUpdate() {
        mode.set(Mode.UPDATE)
        focusMemoEditTextAndShowKeyboard.call()
    }

    private fun clickWriteButton(content: String, currentMemoId: Long?) {
        if (mode.get() == Mode.CREATE) {
            writeMemo(content, currentMemoId)
        } else if (mode.get() == Mode.UPDATE) {
            updateMemo(content)
        }
    }

    private fun writeMemo(content: String, currentMemoId: Long?) {
        val memo = Memo(content = content, parentId = currentMemoId, childMemoContentList = emptyList(), createdDate = Date())
        compositeDisposable += insertMemo.get(memo)
            .subscribe({ id ->
                memo.id = id
                addNewMemoToRecyclerView.onNext(memo)
                toastWriteSuccessMessage.call()
                hideSoftKeyboard.call()
            }) { throwable ->
                throwable.message?.let(toastErrorMessage::onNext)
            }
    }

    private fun updateMemo(content: String) {
        memoToUpdate?.let {
            it.content = content
            compositeDisposable += updateMemo.get(it)
                .subscribe({
                    updateMemoToRecyclerView.onNext(it)
                    toastUpdateSuccessMessage.call()
                    hideSoftKeyboard.call()
                }) { throwable ->
                    throwable.message?.let(toastErrorMessage::onNext)
                }
        }
    }

    private fun onDeleteFromRecyclerView(memo: Memo) {
        if (memo.completedDate == null) {
            completeMemo(memo)
        } else {
            deleteMemoFromDatabase(memo)
        }
    }

    private fun deleteMemoFromDatabase(memo: Memo) {
        compositeDisposable += deleteMemo.get(memo)
            .subscribe({
                toastDeleteSuccessMessage.call()
            }) {
                addNewMemoToRecyclerView.onNext(memo)
                toastDeleteFailMessage.call()
            }
    }

    private fun completeMemo(memo: Memo) {
        compositeDisposable += completeMemo.get(memo)
            .subscribe ({
                addCompleteMemoToRecyclerView.onNext(it)
                toastDeleteSuccessMessage.call()
            }) {
                addNewMemoToRecyclerView.onNext(memo)
                toastDeleteFailMessage.call()
            }
    }

    enum class Mode { NORMAL, CREATE, UPDATE }

}

fun PublishSubject<Unit>.call() = this.onNext(Unit)

interface MemoViewModelInputs {
    fun changeModeToNormal()
    fun changeModeToCreate()
    fun changeModeToUpdate(memoToUpdate: Memo)
    fun clickWriteButton(content: String, currentMemoId: Long? = null)
    fun onDeleteFromRecyclerView(memo: Memo)
}

interface MemoViewModelOutputs {
    fun mode(): ObservableField<MemoViewModel.Mode>
    fun toastErrorMessage(): PublishSubject<String>
    fun toastDeleteSuccessMessage(): PublishSubject<Unit>
    fun toastDeleteFailMessage(): PublishSubject<Unit>
    fun toastWriteSuccessMessage(): PublishSubject<Unit>
    fun toastUpdateSuccessMessage(): PublishSubject<Unit>
    fun hideSoftKeyboard(): PublishSubject<Unit>
    fun focusMemoEditTextAndShowKeyboard(): PublishSubject<Unit>
    fun addNewMemoToRecyclerView(): PublishSubject<Memo>
    fun addCompleteMemoToRecyclerView(): PublishSubject<Memo>
    fun updateMemoToRecyclerView(): PublishSubject<Memo>
}