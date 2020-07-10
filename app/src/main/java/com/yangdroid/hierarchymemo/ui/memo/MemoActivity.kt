package com.yangdroid.hierarchymemo.ui.memo

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.component.BaseActivity
import com.yangdroid.hierarchymemo.databinding.ActivityMemoBinding
import com.yangdroid.hierarchymemo.extension.*
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.main.MemoRecyclerAdapter
import com.yangdroid.hierarchymemo.ui.model.parcelable.ParcelableMemo
import com.yangdroid.hierarchymemo.ui.model.parcelable.toEntity
import com.yangdroid.hierarchymemo.ui.model.parcelable.toParcel
import kotlinx.android.synthetic.main.activity_memo.*
import org.jetbrains.anko.startActivity
import javax.inject.Inject

class MemoActivity : BaseActivity(), MemoContract.View {

    @Inject
    lateinit var presenter: MemoPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityMemoBinding>(this, R.layout.activity_memo).apply {
            presenter = this@MemoActivity.presenter
            lifecycleOwner = this@MemoActivity
        }

        initViews()
        intent.getParcelableExtra<ParcelableMemo>(EXTRA_DATA_CURRENT_MEMO)!!.let {
            presenter.onCreate(it.toEntity())
        }
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
        initMemoRecyclerView()
        iv_memo_edit_write.setOnClickListener { onClickWriteButton() }
        iv_memo_close.setOnClickListener { finish() }
        iv_memo_all_expand.setOnClickListener { getMemoAdapter().expandAll() }
        iv_memo_all_shrink.setOnClickListener { getMemoAdapter().shrinkAll() }
    }

    private fun onClickWriteButton() {
        val memoContent = et_memo_edit.text.toString()
        if (memoContent.isEmpty()) {
            hideSoftKeyboard()
            showErrorToast(R.string.common_message_error_empty_write)
        } else {
            presenter.writeMemo(memoContent)
        }
    }

    private fun initMemoRecyclerView() {
        rv_memo_memo.adapter = MemoRecyclerAdapter(::onClickMemo, ::onLongClickMemo)
        ItemTouchHelper(itemTouchCallback).attachToRecyclerView(rv_memo_memo)
    }

    override fun showMemoList(memoList: List<Memo>) {
        getMemoAdapter().initMemoList(memoList)
    }

    override fun addNewMemoToRecyclerView(memo: Memo) {
        getMemoAdapter().addMemo(memo)
        showSuccessToast(R.string.common_message_success_write)
    }

    override fun updateMemoToRecyclerView(memo: Memo) {
        getMemoAdapter().updateMemo(memo)
    }

    override fun showErrorMessage(message: String) {
        showErrorToast(message)
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

    override fun showEmptyMessage() {
        tv_memo_empty_message.makeVisible()
    }

    override fun hideEmptyMessage() {
        tv_memo_empty_message.makeGone()
    }

    override fun focusMemoEditText() {
        if (et_memo_edit.requestFocus()) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(et_memo_edit, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun hideSoftKeyboard() {
        hideKeyboard()
    }

    private fun onHideKeyboard() {
        presenter.changeModeToNormal()
        et_memo_edit.text.clear()
    }

    private fun onClickMemo(memo: Memo) {
        startActivity<MemoActivity>(
            Pair(EXTRA_DATA_CURRENT_MEMO, memo.toParcel())
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

    private fun setMemoEditText(content: String) {
        et_memo_edit.run {
            text.clear()
            setText(content)
            setSelection(content.length)
        }
    }

    private fun getMemoAdapter() = rv_memo_memo.adapter as MemoRecyclerAdapter

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
        override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val memoToRemove = getMemoAdapter().getMemo(viewHolder.adapterPosition)
            presenter.onDeleteFromRecyclerView(memoToRemove)
            getMemoAdapter().removeMemo(memoToRemove)
        }
    }

    companion object {
        const val EXTRA_DATA_CURRENT_MEMO = "extraDataCurrentMemo"
    }
}
