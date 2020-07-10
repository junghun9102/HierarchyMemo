package com.yangdroid.hierarchymemo.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.component.BaseActivity
import com.yangdroid.hierarchymemo.databinding.ActivityMainBinding
import com.yangdroid.hierarchymemo.extension.*
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.memo.MemoActivity
import com.yangdroid.hierarchymemo.ui.model.parcelable.toParcel
import com.yangdroid.hierarchymemo.utils.getThisMonthTodoString
import com.yangdroid.hierarchymemo.utils.getThisWeekTodoString
import com.yangdroid.hierarchymemo.utils.getTodayTodoString
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
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
        presenter.loadMemoList()
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
        iv_main_all_expand.setOnClickListener { getMemoAdapter().expandAll() }
        iv_main_all_shrink.setOnClickListener { getMemoAdapter().shrinkAll() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_main_edit.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_message_error_empty_write)
        } else {
            presenter.writeMemo(memoContent)
        }
    }

    private fun initMemoRecyclerView() {
        rv_main_memo.adapter = MemoRecyclerAdapter(::onClickMemo, ::onLongClickMemo)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_main_memo)
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
        getMemoAdapter().initMemoList(memoList)
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

    override fun addNewMemoToRecyclerView(memo: Memo) {
        getMemoAdapter().addMemo(memo)
        showSuccessToast(R.string.common_message_success_write)
    }

    override fun updateMemoToRecyclerView(memo: Memo) {
        getMemoAdapter().updateMemo(memo)
    }

    override fun showDeleteCompleteMessage() {
        showSuccessToast(R.string.common_message_success_delete)
    }

    override fun showDeleteFailMessage() {
        showSuccessToast(R.string.common_message_error_delete_fail)
    }

    override fun showUpdateCompleteMessage() {
        showSuccessToast(R.string.common_message_success_update)
    }

    override fun hideSoftKeyboard() {
        hideKeyboard()
    }

    private fun onClickMemo(memo: Memo) {
        startActivity<MemoActivity>(
            Pair(MemoActivity.EXTRA_DATA_CURRENT_MEMO, memo.toParcel())
        )
    }

    private fun onLongClickMemo(memo: Memo) {
        if (memo.completedDate == null) {
            presenter.changeModeToEdit()
            presenter.setMemoToUpdate(memo)
            setMemoEditText(memo.content)
        } else {
            showToast(R.string.common_message_completed_memo_cannot_edit)
        }
    }

    private fun onHideKeyboard() {
        presenter.changeModeToNormal()
        et_main_edit.text.clear()
    }

    private fun getMemoAdapter() = rv_main_memo.adapter as MemoRecyclerAdapter

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
        override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val memoToRemove = getMemoAdapter().getMemo(viewHolder.adapterPosition)
            presenter.onDeleteFromRecyclerView(memoToRemove)
            getMemoAdapter().removeMemo(memoToRemove)
        }
    }

}
