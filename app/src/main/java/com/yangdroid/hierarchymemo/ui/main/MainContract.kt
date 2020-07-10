package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

interface MainContract {
    interface View: BaseView {
        fun showTodayDate(date: Date)
        fun showMemoList(memoList: List<Memo>)
        fun addNewMemoToRecyclerView(memo: Memo)
        fun updateMemoToRecyclerView(memo: Memo)
        fun showErrorMessage(message: String)
        fun showDeleteCompleteMessage()
        fun showDeleteFailMessage()
        fun showUpdateCompleteMessage()
        fun showEmptyMessage()
        fun hideEmptyMessage()
        fun focusMemoEditText()
        fun hideSoftKeyboard()
    }

    interface Presenter {
        fun loadTodayDate()
        fun loadMemoList()
        fun changeTypeToProgress()
        fun changeTypeToCompleted()
        fun changeModeToEdit()
        fun changeModeToNormal()
        fun writeMemo(content: String)
        fun setMemoToUpdate(memo: Memo)
        fun onDeleteFromRecyclerView(memo: Memo)
    }
}