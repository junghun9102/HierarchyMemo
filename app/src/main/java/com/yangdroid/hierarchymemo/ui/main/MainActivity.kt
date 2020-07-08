package com.yangdroid.hierarchymemo.ui.main

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.component.BaseActivity
import com.yangdroid.hierarchymemo.databinding.ActivityMainBinding
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeVisible
import com.yangdroid.hierarchymemo.extension.showErrorToast
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.utils.getThisMonthTodoString
import com.yangdroid.hierarchymemo.utils.getThisWeekTodoString
import com.yangdroid.hierarchymemo.utils.getTodayTodoString
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainActivity : BaseActivity(), MainContract.View {

    @Inject
    lateinit var presenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            presenter = this@MainActivity.presenter
            lifecycleOwner = this@MainActivity
        }

        initViews()
        presenter.onCreate()
    }

    override fun onResume() {
        super.onResume()
        addKeyboardListener(onHideKeyBoard = ::onHideKeyboard)
    }

    override fun onPause() {
        super.onPause()
        removeKeyboardListener()
    }

    private fun initViews() {
        initTemplateClick()
        initMemoRecyclerView()
    }

    private fun initTemplateClick() {
        tv_main_template_today.setOnClickListener { setMemoEditText(getTodayTodoString()) }
        tv_main_template_week.setOnClickListener { setMemoEditText(getThisWeekTodoString()) }
        tv_main_template_month.setOnClickListener { setMemoEditText(getThisMonthTodoString()) }
        iv_main_edit_write.setOnClickListener { onClickWriteButton() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_main_edit.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_error_message_empty_write)
        } else {
            presenter.writeMemo(memoContent)
        }
    }

    private fun initMemoRecyclerView() {
        rv_main_memo.adapter = MemoRecyclerAdapter()
    }

    private fun setMemoEditText(content: String) {
        et_main_edit.run {
            text.clear()
            setText(content)
            setSelection(content.length)
        }
    }

    override fun showTodayDate(date: Date) {
        val formattedDate = SimpleDateFormat(getString(R.string.main_today_format), Locale.US).format(date)
        val dateString = String.format(getString(R.string.main_today_string), formattedDate)
        tv_main_today.text = dateString
    }

    override fun showMemoList(memoList: List<Memo>) {
        (rv_main_memo.adapter as MemoRecyclerAdapter).initMemoList(memoList)
    }

    override fun focusMemoEditText() {
        if (et_main_edit.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_main_edit, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun showErrorMessage(message: String) {
        showErrorToast(message)
    }

    override fun showEmptyMessage() {
        tv_main_empty_message.makeVisible()
    }

    override fun hideEmptyMessage() {
        tv_main_empty_message.makeGone()
    }

    override fun updateNewMemo(memo: Memo) {
        (rv_main_memo.adapter as MemoRecyclerAdapter).addMemo(memo)
    }

    override fun hideSoftKeyboard() {
        hideKeyboard()
    }

    private fun onHideKeyboard() {
        presenter.changeModeToNormal()
        et_main_edit.text.clear()
    }

}
