package com.yangdroid.hierarchymemo.ui.memo

import com.yangdroid.hierarchymemo.component.BaseView
import com.yangdroid.hierarchymemo.model.domain.entity.Memo

interface MemoContract {
    interface View: BaseView {
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
        fun loadMemoList()
        fun changeModeToEdit()
        fun changeModeToNormal()
        fun writeMemo(content: String)
        fun setMemoToUpdate(memo: Memo)
        fun onDeleteFromRecyclerView(memo: Memo)
    }
}