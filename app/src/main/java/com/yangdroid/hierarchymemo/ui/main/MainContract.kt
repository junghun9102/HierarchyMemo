package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

interface MainContract {
    interface View: BaseView {
        fun showTodayDate(date: Date)
        fun showMemoList(memoList: List<Memo>)
        fun focusMemoEditText()
        fun showErrorMessage(message: String)
        fun showEmptyMessage()
        fun hideEmptyMessage()
        fun updateNewMemo(memo: Memo)
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
    }
}