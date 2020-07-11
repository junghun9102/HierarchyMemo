package com.yangdroid.hierarchymemo.ui.main

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

interface MainContract {
    interface View: BaseView {
        fun showTodayDate(date: Date)
        fun showMemoList(memoList: List<Memo>)
        fun toastLoadErrorMessage(message: String)
    }

    interface Presenter {
        fun onCreate()
    }
}