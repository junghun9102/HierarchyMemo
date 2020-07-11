package com.yangdroid.hierarchymemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.databinding.ActivityMainBinding
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeVisible
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.extension.showErrorToast
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.component.memo.MemoActivity
import com.yangdroid.hierarchymemo.ui.component.memo.MemoViewModel
import com.yangdroid.hierarchymemo.utils.getThisMonthTodoString
import com.yangdroid.hierarchymemo.utils.getThisWeekTodoString
import com.yangdroid.hierarchymemo.utils.getTodayTodoString
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : MemoActivity(), MainContract.View {

    @Inject
    override lateinit var memoViewModel: MemoViewModel

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            memoViewModel = this@MainActivity.memoViewModel
            presenter = this@MainActivity.presenter
            lifecycleOwner = this@MainActivity
        }

        initViews()
        subscribeMemoViewModel()

        lifecycle += presenter
        presenter.onCreate()
    }

    private fun initViews() {
        initTemplateClickListener()
        initToolsClickListener()
        initMemoRecyclerView()
    }

    private fun initTemplateClickListener() {
        tv_main_template_today.setOnClickListener { setMemoEditText(getTodayTodoString()) }
        tv_main_template_week.setOnClickListener { setMemoEditText(getThisWeekTodoString()) }
        tv_main_template_month.setOnClickListener { setMemoEditText(getThisMonthTodoString()) }
    }

    private fun initToolsClickListener() {
        iv_main_edit_write.setOnClickListener { onClickWriteButton() }
        iv_main_all_expand.setOnClickListener { getRecyclerAdapter().expandAll() }
        iv_main_all_shrink.setOnClickListener { getRecyclerAdapter().shrinkAll() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_main_edit.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_message_error_empty_write)
        } else {
            memoViewModel.input.clickWriteButton(memoContent)
        }
    }

    private fun initMemoRecyclerView() {
        rv_main_memo.adapter = MemoRecyclerAdapter(::onClickMemo, ::onLongClickMemo)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_main_memo)
    }

    override fun showTodayDate(date: Date) {
        val formattedDate = SimpleDateFormat(getString(R.string.main_today_format), Locale.US).format(date)
        val dateString = String.format(getString(R.string.main_today_string), formattedDate)
        tv_main_today.text = dateString
    }

    override fun showMemoList(memoList: List<Memo>) {
        getRecyclerAdapter().initMemoList(memoList)
        checkListEmptyAndSetEmptyMessageVisible()
    }

    override fun toastLoadErrorMessage(message: String) {
        showErrorToast(message)
    }

    override fun setMemoEditText(content: String) {
        et_main_edit.run {
            text.clear()
            setText(content)
            setSelection(content.length)
        }
    }

    override fun focusMemoEditTextAndShowKeyboard() {
        if (et_main_edit.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_main_edit, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onHideSoftKeyboard() {
        memoViewModel.input.changeModeToNormal()
        et_main_edit.text.clear()
    }

    override fun checkListEmptyAndSetEmptyMessageVisible() {
        if (getRecyclerAdapter().isEmpty()) {
            tv_main_empty_message.makeVisible()
        } else {
            tv_main_empty_message.makeGone()
        }
    }

    override fun getRecyclerAdapter(): MemoRecyclerAdapter = rv_main_memo.adapter as MemoRecyclerAdapter

}
