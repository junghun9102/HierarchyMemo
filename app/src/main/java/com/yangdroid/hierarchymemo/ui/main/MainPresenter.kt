package com.yangdroid.hierarchymemo.ui.main

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.Constants
import com.yangdroid.hierarchymemo.component.BasePresenter
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.usecase.*
import java.util.*

class MainPresenter(
    view: MainContract.View,
    private val getRootProgressMemoList: GetRootProgressMemoList,
    private val getRootCompletedMemoList: GetRootCompletedMemoList,
    private val insertMemo: InsertMemo,
    private val deleteMemo: DeleteMemo,
    private val completeMemo: CompleteMemo,
    private val updateMemo: UpdateMemo
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    val mode = ObservableField<Constants.Mode>(Constants.Mode.NORMAL)
    val type = ObservableField<MemoTypeToLoad>(MemoTypeToLoad.PROGRESS)

    private var memoToEdit: Memo? = null

    fun onCreate() {
        loadTodayDate()
        loadMemoList()
    }

    override fun loadTodayDate() {
        view.showTodayDate(Date())
    }

    override fun loadMemoList() {
        when (type.get()) {
            MemoTypeToLoad.PROGRESS -> loadProgressMemoList()
            MemoTypeToLoad.COMPLETED -> loadCompletedMemoList()
        }
    }

    private fun loadProgressMemoList() {
        compositeDisposable += getRootProgressMemoList.get()
            .subscribe({ result ->
                view.showMemoList(result)
                showOrHideEmptyMessage(result.isEmpty())
            }) {
                it.message?.let(view::showErrorMessage)
            }
    }

    private fun loadCompletedMemoList() {
        compositeDisposable += getRootCompletedMemoList.get()
            .subscribe({ result ->
                view.showMemoList(result)
                showOrHideEmptyMessage(result.isEmpty())
            }) {
                it.message?.let(view::showErrorMessage)
            }
    }

    private fun showOrHideEmptyMessage(isEmpty: Boolean) {
        if (isEmpty) {
            view.showEmptyMessage()
        } else {
            view.hideEmptyMessage()
        }
    }

    override fun changeTypeToProgress() {
        type.set(MemoTypeToLoad.PROGRESS)
        loadMemoList()
    }

    override fun changeTypeToCompleted() {
        type.set(MemoTypeToLoad.COMPLETED)
        loadMemoList()
    }

    override fun changeModeToEdit() {
        mode.set(Constants.Mode.EDIT)
        view.focusMemoEditText()
    }

    override fun changeModeToNormal() {
        mode.set(Constants.Mode.NORMAL)
        memoToEdit = null
    }

    override fun setMemoToUpdate(memo: Memo) {
        memoToEdit = memo
    }

    override fun writeMemo(content: String) {
        memoToEdit?.let {
            it.content = content
            compositeDisposable += updateMemo.get(it)
                .subscribe({
                    view.updateMemoToRecyclerView(it)
                    view.showUpdateCompleteMessage()
                    view.hideSoftKeyboard()
                }) { throwable ->
                    throwable.message?.let(view::showErrorMessage)
                }
        } ?: run {
            val memo =
                Memo(content = content, childMemoContentList = emptyList(), createdDate = Date())
            compositeDisposable += insertMemo.get(memo)
                .subscribe({ id ->
                    memo.id = id
                    view.addNewMemoToRecyclerView(memo)
                    view.hideEmptyMessage()
                    view.hideSoftKeyboard()
                }) {
                    it.message?.let(view::showErrorMessage)
                }
        }
    }

    override fun onDeleteFromRecyclerView(memo: Memo) {
        if (memo.completedDate == null) {
            completeMemo(memo)
        } else {
            deleteMemoFromDatabase(memo)
        }
    }

    private fun deleteMemoFromDatabase(memo: Memo) {
        compositeDisposable += deleteMemo.get(memo)
            .subscribe({
                view.showDeleteCompleteMessage()
            }) {
                loadMemoList()
                view.showDeleteFailMessage()
            }
    }

    private fun completeMemo(memo: Memo) {
        compositeDisposable += completeMemo.get(memo)
            .subscribe ({
                view.showDeleteCompleteMessage()
            }) {
                loadMemoList()
                view.showDeleteFailMessage()
            }
    }

    enum class MemoTypeToLoad { PROGRESS, COMPLETED }

}