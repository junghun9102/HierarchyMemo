package com.yangdroid.hierarchymemo.ui.main

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.component.BaseActivity
import com.yangdroid.hierarchymemo.databinding.ActivityMainBinding
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.utils.*
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : BaseActivity(), MainContract.View {

    private val presenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main).apply {
            presenter = this@MainActivity.presenter
        }

        presenter.onCreate()
        initViews()
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
    }

    private fun initTemplateClick() {
        tv_main_template_today.setOnClickListener { setMemoEditText(getTodayTodoString()) }
        tv_main_template_week.setOnClickListener { setMemoEditText(getThisWeekTodoString()) }
        tv_main_template_month.setOnClickListener { setMemoEditText(getThisMonthTodoString()) }
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

    override fun showProgressMemoList(memoList: List<Memo>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showCompletedMemoList(memoList: List<Memo>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun focusMemoEditText() {
        if (et_main_edit.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_main_edit, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun onHideKeyboard() {
        presenter.changeModeToNormal()
        et_main_edit.text.clear()
    }
}
