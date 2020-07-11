package com.yangdroid.hierarchymemo.ui.memo

import com.yangdroid.hierarchymemo.component.BasePresenter
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.model.domain.usecase.GetChildMemoList

class SubMemoPresenter(
    view: SubMemoContract.View,
    private val getChildMemoList: GetChildMemoList
) : BasePresenter<SubMemoContract.View>(view), SubMemoContract.Presenter {

    private lateinit var currentMemo: Memo

    override fun onCreate(memo: Memo) {
        currentMemo = memo
        loadMemoList()
    }

    override fun getCurrentMemoId(): Long = currentMemo.id!!

    override fun getTitle(): String = currentMemo.content

    private fun loadMemoList() {
        compositeDisposable += getChildMemoList.get(getCurrentMemoId())
            .subscribe({ result ->
                view.showMemoList(result)
            }) {
                it.message?.let(view::toastLoadErrorMessage)
            }
    }

}