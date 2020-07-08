package com.yangdroid.hierarchymemo.ui.main

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.component.BasePresenter
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootCompletedMemoList
import com.yangdroid.hierarchymemo.model.domain.usecase.GetRootProgressMemoList
import com.yangdroid.hierarchymemo.model.domain.usecase.InsertMemo
import java.util.*

class MainPresenter(
    view: MainContract.View,
    private val getRootProgressMemoList: GetRootProgressMemoList,
    private val getRootCompletedMemoList: GetRootCompletedMemoList,
    private val insertMemo: InsertMemo
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    val mode = ObservableField<Mode>(Mode.NORMAL)
    val type = ObservableField<MemoTypeToLoad>(MemoTypeToLoad.PROGRESS)

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
        mode.set(Mode.EDIT)
        view.focusMemoEditText()
    }

    override fun changeModeToNormal() {
        mode.set(Mode.NORMAL)
    }

    override fun writeMemo(content: String) {
        val memo = Memo(content = content, childMemoContentList = emptyList(), createdDate = Date())
        compositeDisposable += insertMemo.get(memo)
            .subscribe ({ id ->
                memo.id = id
                view.updateNewMemo(memo)
                view.hideEmptyMessage()
                view.hideSoftKeyboard()
            }) {
                it.message?.let(view::showErrorMessage)
            }
    }

    enum class MemoTypeToLoad { PROGRESS, COMPLETED }
    enum class Mode { NORMAL, EDIT }

}