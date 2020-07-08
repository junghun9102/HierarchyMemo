package com.yangdroid.hierarchymemo.ui.main

import androidx.databinding.ObservableField
import com.yangdroid.hierarchymemo.component.BasePresenter
import java.util.*

class MainPresenter(
    view: MainContract.View
) : BasePresenter<MainContract.View>(view), MainContract.Presenter {

    val mode = ObservableField<Mode>(Mode.NORMAL)
    val type = ObservableField<MemoTypeToLoad>(MemoTypeToLoad.PROGRESS)

    fun onCreate() {
        loadTodayDate()
    }

    override fun loadTodayDate() {
        view.showTodayDate(Date())
    }

    override fun loadMemoList() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun changeTypeToProgress() {
        type.set(MemoTypeToLoad.PROGRESS)
    }

    override fun changeTypeToCompleted() {
        type.set(MemoTypeToLoad.COMPLETED)
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