package com.yangdroid.hierarchymemo.ui.component.memo

import android.app.Activity
import android.content.Intent
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.yangdroid.hierarchymemo.Constants
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.component.BaseActivity
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.extension.showErrorToast
import com.yangdroid.hierarchymemo.extension.showSuccessToast
import com.yangdroid.hierarchymemo.extension.showToast
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.main.MemoRecyclerAdapter
import com.yangdroid.hierarchymemo.ui.memo.SubMemoActivity
import com.yangdroid.hierarchymemo.ui.model.parcelable.toParcel

abstract class MemoActivity : BaseActivity() {

    abstract var memoViewModel: MemoViewModel

    override fun onResume() {
        super.onResume()
        addKeyboardListener(onHideSoftKeyboard = ::onHideSoftKeyboard)
    }

    override fun onPause() {
        super.onPause()
        removeKeyboardListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        memoViewModel.onCleared()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODE_MEMO_STACK && resultCode == Activity.RESULT_OK) {
            data?.run {
                val id = getLongExtra(EXTRA_DATA_RETURN_MEMO_ID, DEFAULT_MEMO_ID)
                if (id != DEFAULT_MEMO_ID) {
                    getStringArrayExtra(EXTRA_DATA_RETURN_MEMO_CHILD_CONTENT_LIST)?.let {
                        getRecyclerAdapter().updateMemoChildContentList(id, it.toList())
                    }
                }
            }
        }
    }

    protected fun onClickMemo(memo: Memo) {
        startActivityForResult(Intent(this, SubMemoActivity::class.java).apply {
            putExtra(SubMemoActivity.EXTRA_DATA_CURRENT_MEMO, memo.toParcel())
        }, Constants.REQUEST_CODE_MEMO_STACK)
    }

    protected fun onLongClickMemo(memo: Memo) {
        if (memo.completedDate == null) {
            memoViewModel.input.changeModeToUpdate(memo)
            setMemoEditText(memo.content)

        } else {
            showToast(R.string.common_message_completed_memo_cannot_edit)
        }
    }

    protected fun subscribeMemoViewModel() {
        memoViewModel.output.run {
            disposables += toastErrorMessage().subscribe(this@MemoActivity::toastErrorMessage)
            disposables += toastDeleteSuccessMessage().subscribe { this@MemoActivity.toastDeleteSuccessMessage() }
            disposables += toastDeleteFailMessage().subscribe { this@MemoActivity.toastDeleteFailMessage() }
            disposables += toastUpdateSuccessMessage().subscribe { this@MemoActivity.toastUpdateSuccessMessage() }
            disposables += toastWriteSuccessMessage().subscribe { this@MemoActivity.toastWriteSuccessMessage() }
            disposables += hideSoftKeyboard().subscribe { this@MemoActivity.hideSoftKeyboard() }
            disposables += addNewMemoToRecyclerView().subscribe {
                getRecyclerAdapter().addMemo(it)
                checkListEmptyAndSetEmptyMessageVisible()
            }
            disposables += updateMemoToRecyclerView().subscribe(getRecyclerAdapter()::updateMemo)
            disposables += focusMemoEditTextAndShowKeyboard().subscribe { this@MemoActivity.focusMemoEditTextAndShowKeyboard() }
        }
    }

    private fun toastErrorMessage(message: String) {
        showErrorToast(message)
    }

    private fun toastDeleteSuccessMessage() {
        showSuccessToast(R.string.common_message_success_delete)
    }

    private fun toastDeleteFailMessage() {
        showSuccessToast(R.string.common_message_error_delete_fail)
    }

    private fun toastUpdateSuccessMessage() {
        showSuccessToast(R.string.common_message_success_update)
    }

    private fun toastWriteSuccessMessage() {
        showSuccessToast(R.string.common_message_success_write)
    }

    protected val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.START or ItemTouchHelper.END) {
        override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val memoToRemove = getRecyclerAdapter().getMemo(viewHolder.adapterPosition)
            memoViewModel.input.onDeleteFromRecyclerView(memoToRemove)
            getRecyclerAdapter().removeMemo(memoToRemove)
            checkListEmptyAndSetEmptyMessageVisible()
        }
    }

    abstract fun setMemoEditText(content: String)
    abstract fun focusMemoEditTextAndShowKeyboard()
    abstract fun onHideSoftKeyboard()
    abstract fun checkListEmptyAndSetEmptyMessageVisible()
    abstract fun getRecyclerAdapter(): MemoRecyclerAdapter

    companion object {
        const val DEFAULT_MEMO_ID = -1L
        const val EXTRA_DATA_RETURN_MEMO_ID = "extraDataReturnMemoId"
        const val EXTRA_DATA_RETURN_MEMO_CHILD_CONTENT_LIST = "extraDataReturnMemoChildContentList"
    }

}