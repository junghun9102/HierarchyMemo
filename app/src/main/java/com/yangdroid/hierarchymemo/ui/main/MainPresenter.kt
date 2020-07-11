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
    private val getRootCompletedMemoList: GetRootCompletedMemoList
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    val type = ObservableField<MemoTypeToLoad>(MemoTypeToLoad.PROGRESS)

    override fun onCreate() {
        loadTodayDate()
        loadMemoList()
    }

    private fun loadTodayDate() {
        view.showTodayDate(Date())
    }

    private fun loadMemoList() {
        when (type.get()) {
            MemoTypeToLoad.PROGRESS -> loadProgressMemoList()
            MemoTypeToLoad.COMPLETED -> loadCompletedMemoList()
        }
    }

    private fun loadProgressMemoList() {
        compositeDisposable += getRootProgressMemoList.get()
            .subscribe({ result ->
                view.showMemoList(result)
            }) {
                it.message?.let(view::toastLoadErrorMessage)
            }
    }

    private fun loadCompletedMemoList() {
        compositeDisposable += getRootCompletedMemoList.get()
            .subscribe({ result ->
                view.showMemoList(result)
            }) {
                it.message?.let(view::toastLoadErrorMessage)
            }
    }

    fun changeTypeToProgress() {
        type.set(MemoTypeToLoad.PROGRESS)
        loadMemoList()
    }

    fun changeTypeToCompleted() {
        type.set(MemoTypeToLoad.COMPLETED)
        loadMemoList()
    }

    enum class MemoTypeToLoad { PROGRESS, COMPLETED }

}