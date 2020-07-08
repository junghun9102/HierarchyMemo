package com.yangdroid.hierarchymemo.ui.main

import android.util.Log
import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.component.BasePresenter
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import java.util.*

class MainPresenter(
    view: MainContract.View
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    val mode = ObservableField<Mode>(Mode.NORMAL)
    val type = ObservableField<MemoTypeToLoad>(MemoTypeToLoad.PROGRESS)

    fun onCreate() {
        loadTodayDate()
        loadMemoList()
        Log.e("Test", "onCreate")
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

    // TODO : temp
    val subList = listOf(
        Memo(1, "자료구조", emptyList(), Date(), null),
        Memo(2, "알고리즘", emptyList(), Date(), null),
        Memo(3, "운영체제", emptyList(), Date(), null),
        Memo(4, "네트워크", emptyList(), Date(), null)
    )

    val list = listOf(
        Memo(5, "취업하기", emptyList(), Date(), null),
        Memo(6, "프로그래밍 공부하기", subList, Date(), null)
    )

    private fun loadProgressMemoList() {
        Log.e("Test", "loadProgressMemoList")
        view.showMemoList(list)
    }

    private fun loadCompletedMemoList() {
        view.showMemoList(emptyList())
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

    enum class MemoTypeToLoad { PROGRESS, COMPLETED }
    enum class Mode { NORMAL, EDIT }

}