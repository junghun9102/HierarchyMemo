package com.yangdroid.hierarchymemo.ui.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeInvisible
import com.yangdroid.hierarchymemo.extension.makeVisible
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.ui.model.MemoBoxed
import com.yangdroid.hierarchymemo.ui.model.boxing
import kotlinx.android.synthetic.main.item_memo.view.*

class MemoRecyclerAdapter : RecyclerView.Adapter<MemoRecyclerAdapter.MemoHolder>() {

    private val dataSet = mutableListOf<MemoBoxed>()

    override fun getItemCount(): Int = dataSet.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemoHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_memo, parent, false)
        val holder = MemoHolder(view)
        view.run {
            iv_item_memo_expand_plus.setOnClickListener { onClickExpandUI(holder.adapterPosition) }
            cl_item_memo_expand_minus.setOnClickListener { onClickExpandUI(holder.adapterPosition) }
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
            addAll(memoList.map(Memo::boxing))
        }
        Log.e("Test", "${dataSet.size}")
        notifyDataSetChanged()
    }

    fun addMemo(memo: Memo) {
        dataSet.add(memo.boxing())
        notifyItemInserted(dataSet.lastIndex)
    }

    fun removeMemo(memo: Memo) {
        dataSet.find { it.id == memo.id }?.let { memoToRemove ->
            val indexToRemove = dataSet.indexOf(memoToRemove)
            dataSet.removeAt(indexToRemove)
            notifyItemRemoved(indexToRemove)
        }
    }

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
                itemView.tv_item_memo_content.text = memo.getSpannableContent(itemView.context)
            } else {
                itemView.tv_item_memo_content.text = memo.content
            }
        }

        private fun updateExpandState(memo: MemoBoxed) {
            val isExpandable = memo.childMemoList.isNotEmpty()
            if (isExpandable) {
                itemView.cl_item_memo_expand.makeVisible()
                if (memo.isExpand) {
                    itemView.iv_item_memo_expand_plus.makeInvisible()
                    itemView.cl_item_memo_expand_minus.makeVisible()
                } else {
                    itemView.iv_item_memo_expand_plus.makeVisible()
                    itemView.cl_item_memo_expand_minus.makeInvisible()
                }
            } else {
                itemView.cl_item_memo_expand.makeInvisible()
            }
        }

    }
}