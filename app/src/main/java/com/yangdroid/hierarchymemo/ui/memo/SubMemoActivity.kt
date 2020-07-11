package com.yangdroid.hierarchymemo.ui.memo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.databinding.ActivityMemoBinding
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeVisible
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.extension.showErrorToast
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.component.memo.MemoActivity
import com.yangdroid.hierarchymemo.ui.component.memo.MemoViewModel
import com.yangdroid.hierarchymemo.ui.main.MemoRecyclerAdapter
import com.yangdroid.hierarchymemo.ui.model.parcelable.ParcelableMemo
import com.yangdroid.hierarchymemo.ui.model.parcelable.toEntity
import kotlinx.android.synthetic.main.activity_memo.*
import javax.inject.Inject

class SubMemoActivity : MemoActivity(), SubMemoContract.View {

    @Inject
    override lateinit var memoViewModel: MemoViewModel

    @Inject
    lateinit var presenter: SubMemoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMemoBinding>(this, R.layout.activity_memo).apply {
            memoViewModel = this@SubMemoActivity.memoViewModel
            presenter = this@SubMemoActivity.presenter
            lifecycleOwner = this@SubMemoActivity
        }

        initViews()
        subscribeMemoViewModel()

        intent.getParcelableExtra<ParcelableMemo>(EXTRA_DATA_CURRENT_MEMO)!!.let {
            lifecycle += presenter
            presenter.onCreate(it.toEntity())
        }
    }

    private fun initViews() {
        initMemoRecyclerView()
        iv_memo_edit_write.setOnClickListener { onClickWriteButton() }
        iv_memo_close.setOnClickListener { onBackPressed() }
        iv_memo_all_expand.setOnClickListener { getRecyclerAdapter().expandAll() }
        iv_memo_all_shrink.setOnClickListener { getRecyclerAdapter().shrinkAll() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_memo_edit.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_message_error_empty_write)
        } else {
            memoViewModel.input.clickWriteButton(memoContent, presenter.getCurrentMemoId())
        }
    }

    private fun initMemoRecyclerView() {
        rv_memo_memo.adapter = MemoRecyclerAdapter(::onClickMemo, ::onLongClickMemo)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_memo_memo)
    }

    override fun showMemoList(memoList: List<Memo>) {
        getRecyclerAdapter().initMemoList(memoList)
        checkListEmptyAndSetEmptyMessageVisible()
    }

    override fun toastLoadErrorMessage(message: String) {
        showErrorToast(message)
    }

    override fun setMemoEditText(content: String) {
        et_memo_edit.run {
            text.clear()
            setText(content)
            setSelection(content.length)
        }
    }

    override fun focusMemoEditTextAndShowKeyboard() {
        if (et_memo_edit.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_memo_edit, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onHideSoftKeyboard() {
        memoViewModel.input.changeModeToNormal()
        et_memo_edit.text.clear()
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent().apply {
            putExtra(EXTRA_DATA_RETURN_MEMO_ID, presenter.getCurrentMemoId())
            putExtra(EXTRA_DATA_RETURN_MEMO_CHILD_CONTENT_LIST, getRecyclerAdapter().getContentList())
        })
        finish()
    }

    override fun checkListEmptyAndSetEmptyMessageVisible() {
        if (getRecyclerAdapter().isEmpty()) {
            tv_memo_empty_message.makeVisible()
        } else {
            tv_memo_empty_message.makeGone()
        }
    }

    override fun getRecyclerAdapter(): MemoRecyclerAdapter = rv_memo_memo.adapter as MemoRecyclerAdapter

    companion object {
        const val EXTRA_DATA_CURRENT_MEMO = "extraDataCurrentMemo"
    }
}
