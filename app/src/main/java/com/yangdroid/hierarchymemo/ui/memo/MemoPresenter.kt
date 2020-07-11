package com.yangdroid.hierarchymemo.ui.memo

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.Constants
import com.yangdroid.hierarchymemo.component.BasePresenter
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.usecase.*
import java.util.*

class MemoPresenter(
    view: MemoContract.View,
    private val getChildMemoList: GetChildMemoList,
    private val insertMemo: InsertMemo,
    private val deleteMemo: DeleteMemo,
    private val completeMemo: CompleteMemo,
    private val updateMemo: UpdateMemo
) : BasePresenter<MemoContract.View>(view), MemoContract.Presenter {

    lateinit var currentMemo: Memo
    val mode = ObservableField<Constants.Mode>(Constants.Mode.NORMAL)

    private var memoToEdit: Memo? = null

    fun onCreate(memo: Memo) {
        currentMemo = memo
        loadMemoList()
    }

    override fun loadMemoList() {
        compositeDisposable += getChildMemoList.get(currentMemo.id!!)
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

    override fun changeModeToEdit() {
        mode.set(Constants.Mode.EDIT)
        view.focusMemoEditText()
    }

    override fun changeModeToNormal() {
        mode.set(Constants.Mode.NORMAL)
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
                Memo(content = content, parentId = currentMemo.id, childMemoContentList = emptyList(), createdDate = Date())
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

    override fun setMemoToUpdate(memo: Memo) {
        memoToEdit = memo
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
                view.addNewMemoToRecyclerView(it)
            }) {
                loadMemoList()
                view.showDeleteFailMessage()
            }
    }

}