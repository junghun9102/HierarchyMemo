package com.yangdroid.hierarchymemo.ui.memo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.databinding.ActivitySubMemoBinding
import com.yangdroid.hierarchymemo.component.ViewModelFactory
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
import kotlinx.android.synthetic.main.activity_sub_memo.*
import javax.inject.Inject

class SubMemoActivity : MemoActivity(), SubMemoContract.View {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var presenter: SubMemoPresenter

    override lateinit var memoViewModel: MemoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

        memoViewModel = ViewModelProviders.of(this, viewModelFactory).get(MemoViewModel::class.java)

        DataBindingUtil.setContentView<ActivitySubMemoBinding>(this, R.layout.activity_sub_memo).apply {
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
        initDefaultClickListener()
        initToolsClickListener()
        initMemoRecyclerView()
    }

    private fun initDefaultClickListener() {
        iv_sub_memo_close.setOnClickListener { onBackPressed() }
        cl_sub_memo_edit_mode.setOnClickListener {
            memoViewModel.input.changeModeToNormal()
            hideSoftKeyboard()
        }
    }

    private fun initToolsClickListener() {
        iv_sub_memo_edit_mode_write.setOnClickListener { onClickWriteButton() }
        iv_sub_memo_all_expand.setOnClickListener { getRecyclerAdapter().expandAll() }
        iv_sub_memo_all_shrink.setOnClickListener { getRecyclerAdapter().shrinkAll() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_sub_memo_edit_mode_write.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_message_error_empty_write)
        } else {
            memoViewModel.input.clickWriteButton(memoContent, presenter.getCurrentMemoId())
        }
    }

    private fun initMemoRecyclerView() {
        rv_sub_memo_memo.adapter = MemoRecyclerAdapter(::onClickMemo, ::onLongClickMemo)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_sub_memo_memo)
    }

    override fun showMemoList(memoList: List<Memo>) {
        getRecyclerAdapter().initMemoList(memoList)
        checkListEmptyAndSetEmptyMessageVisible()
    }

    override fun toastLoadErrorMessage(message: String) {
        showErrorToast(message)
    }

    override fun setMemoEditText(content: String) {
        et_sub_memo_edit_mode_write.run {
            text.clear()
            setText(content)
            setSelection(content.length)
        }
    }

    override fun subscribeMemoViewModel() {
        super.subscribeMemoViewModel()
        disposables += memoViewModel.output.addCompleteMemoToRecyclerView().subscribe {
            getRecyclerAdapter().addMemo(it)
            checkListEmptyAndSetEmptyMessageVisible()
        }
    }

    override fun focusMemoEditTextAndShowKeyboard() {
        if (et_sub_memo_edit_mode_write.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_sub_memo_edit_mode_write, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onHideSoftKeyboard() {
        memoViewModel.input.changeModeToNormal()
        et_sub_memo_edit_mode_write.text.clear()
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
            tv_sub_memo_empty_message.makeVisible()
        } else {
            tv_sub_memo_empty_message.makeGone()
        }
    }

    override fun getRecyclerAdapter(): MemoRecyclerAdapter = rv_sub_memo_memo.adapter as MemoRecyclerAdapter

    companion object {
        const val EXTRA_DATA_CURRENT_MEMO = "extraDataCurrentMemo"
    }
}
