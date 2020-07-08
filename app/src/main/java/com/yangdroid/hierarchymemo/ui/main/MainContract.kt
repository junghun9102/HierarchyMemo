package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

interface MainContract {
    interface View: BaseView {
        fun showTodayDate(date: Date)
        fun showProgressMemoList(memoList: List<Memo>)
        fun showCompletedMemoList(memoList: List<Memo>)
        fun focusMemoEditText()
    }

    interface Presenter {
        fun loadTodayDate()
        fun loadMemoList()
        fun changeTypeToProgress()
        fun changeTypeToCompleted()
        fun changeModeToEdit()
        fun changeModeToNormal()
    }
}