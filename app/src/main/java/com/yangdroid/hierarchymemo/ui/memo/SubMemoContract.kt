package com.yangdroid.hierarchymemo.ui.memo

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo

interface SubMemoContract {
    interface View: BaseView {
        fun showMemoList(memoList: List<Memo>)
        fun toastLoadErrorMessage(message: String)
    }

    interface Presenter {
        fun onCreate(memo: Memo)
        fun getCurrentMemoId(): Long
        fun getTitle(): String
    }
}