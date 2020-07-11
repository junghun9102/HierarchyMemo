package com.yangdroid.hierarchymemo.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeInvisible
import com.yangdroid.hierarchymemo.extension.makeVisible
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.model.MemoBoxed
import com.yangdroid.hierarchymemo.ui.model.boxing
import com.yangdroid.hierarchymemo.ui.model.unboxing
import kotlinx.android.synthetic.main.item_memo.view.*

class MemoRecyclerAdapter(
    private val onClickMemo: (memo: Memo) -> Unit,
    private val onLongClickMemo: (memo: Memo) -> Unit
) : RecyclerView.Adapter<MemoRecyclerAdapter.MemoHolder>() {

    private val dataSet = mutableListOf<MemoBoxed>()

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
        val holder = MemoHolder(view)
        view.run {
            fl_item_memo_expand.setOnClickListener { onClickExpandUI(holder.adapterPosition) }
            setOnClickListener { onClickMemo.invoke(dataSet[holder.adapterPosition].unboxing()) }
            setOnLongClickListener {
                onLongClickMemo.invoke(dataSet[holder.adapterPosition].unboxing())
                true
            }
        }
        return holder
    }

    private fun onClickExpandUI(position: Int) {
        val memo = dataSet[position]
        memo.isExpand = !memo.isExpand
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: MemoHolder, position: Int) {
        dataSet[position].let {
            holder.onBind(it)
        }
    }

    fun initMemoList(memoList: List<Memo>) {
        dataSet.run {
            clear()
            addAll(memoList.map(Memo::boxing).sortedBy { it.completedDate })
        }
        notifyDataSetChanged()
    }

    fun addMemo(memo: Memo) {
        val firstCompleteMemo = dataSet.firstOrNull { it.completedDate != null }
        val index = if (memo.completedDate == null && firstCompleteMemo != null) {
            dataSet.indexOf(firstCompleteMemo)
        } else {
            dataSet.lastIndex+1
        }
        dataSet.add(index, memo.boxing())
        notifyItemInserted(index)
    }

    fun updateMemo(memo: Memo) {
        dataSet.find { it.id == memo.id }?.let { memoToUpdate ->
            val indexToUpdate = dataSet.indexOf(memoToUpdate)
            memoToUpdate.content = memo.content
            notifyItemChanged(indexToUpdate)
        }
    }

    fun updateMemoChildContentList(memoId: Long, memoChildContentList: List<String>) {
        dataSet.find { it.id == memoId}?.let { memoToUpdate ->
            val indexToUpdate = dataSet.indexOf(memoToUpdate)
            memoToUpdate.childMemoContentList = memoChildContentList
            notifyItemChanged(indexToUpdate)
        }
    }

    fun removeMemo(memo: Memo) {
        dataSet.find { it.id == memo.id }?.let { memoToRemove ->
            val indexToRemove = dataSet.indexOf(memoToRemove)
            dataSet.removeAt(indexToRemove)
            notifyItemRemoved(indexToRemove)
        }
    }

    fun expandAll() {
        dataSet.filter { it.childMemoContentList.isNotEmpty() }
            .filter { it.isExpand.not() }
            .forEach {
                it.isExpand = true
                val index = dataSet.indexOf(it)
                notifyItemChanged(index)
            }
    }

    fun shrinkAll() {
        dataSet.filter { it.childMemoContentList.isNotEmpty() }
            .filter { it.isExpand }
            .forEach {
                it.isExpand = false
                val index = dataSet.indexOf(it)
                notifyItemChanged(index)
            }
    }

    fun getContentList() = dataSet.filter { it.completedDate == null }
        .map { it.content }.toTypedArray()

    fun getMemo(position: Int) = dataSet[position].unboxing()

    fun isEmpty() = dataSet.isEmpty()

    class MemoHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun onBind(memo: MemoBoxed) {
            updateContent(memo)
            updateDateInform(memo)
            updateExpandState(memo)
        }

        private fun updateDateInform(memo: MemoBoxed) {
            if (memo.completedDate == null) {
                itemView.ll_item_memo_edit_inform.makeGone()
            } else {
                val context = itemView.context
                itemView.ll_item_memo_edit_inform.makeVisible()
                itemView.ll_item_memo_edit_inform_created_date.text = memo.getCreatedDate(context)
                itemView.ll_item_memo_edit_inform_completed_date.text = memo.getCompletedDate(context)
            }
        }

        private fun updateContent(memo: MemoBoxed) {
            if (memo.isExpand) {
                itemView.ll_item_memo_expand_content.makeVisible()
                itemView.tv_item_memo_content.text = memo.getSpannableMemo(itemView.context)
                itemView.tv_item_memo_expand_content_child.text = memo.getSpannableChildMemo(itemView.context)
            } else {
                itemView.ll_item_memo_expand_content.makeGone()
                itemView.tv_item_memo_content.text = memo.content
                val textColor = if (memo.completedDate == null) R.color.colorBlue else R.color.colorGrey
                itemView.tv_item_memo_content.setTextColor(ContextCompat.getColor(itemView.context, textColor))
            }
        }

        private fun updateExpandState(memo: MemoBoxed) {
            val isExpandable = memo.childMemoContentList.isNotEmpty()
            if (isExpandable) {
                itemView.fl_item_memo_expand.makeVisible()
                if (memo.isExpand) {
                    itemView.iv_item_memo_expand_plus.makeInvisible()
                    itemView.cl_item_memo_expand_minus.makeVisible()
                } else {
                    itemView.iv_item_memo_expand_plus.makeVisible()
                    itemView.cl_item_memo_expand_minus.makeInvisible()
                }
            } else {
                itemView.fl_item_memo_expand.makeInvisible()
            }
        }

    }
}