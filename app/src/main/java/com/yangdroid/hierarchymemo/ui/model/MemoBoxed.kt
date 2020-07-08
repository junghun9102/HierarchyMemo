package com.yangdroid.hierarchymemo.ui.model

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import androidx.core.content.ContextCompat
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.model.domain.entity.Memo
import com.yangdroid.hierarchymemo.utils.StringUtils
import java.lang.StringBuilder
import java.util.*

data class MemoBoxed(
    var id: Long?,
    var parentId: Long?,
    var content: String,
    val childMemoContentList: List<String>,
    var createdDate: Date,
    var completedDate: Date?,
    var isExpand: Boolean = false
) {

    companion object {
        const val SPANNABLE_PREFIX = " {\n"
        const val SPANNABLE_SUFFIX = "}"
        const val SPANNABLE_SPLIT = ","
    }

    fun getSpannableContent(context: Context): SpannableString {
        val mainStrColor = ContextCompat.getColor(context, R.color.colorBlue)
        val subStrColor = ContextCompat.getColor(context, R.color.colorPurple)
        val specialCharColor = ContextCompat.getColor(context, R.color.colorSky)

        val originString = StringBuilder()

        // <colorResId, startIndex, endIndex>
        val colorInformList = mutableListOf<Triple<Int, Int, Int>>()

        var index = 0
        originString.append(content)
        colorInformList.add(Triple(mainStrColor, index, originString.length))
        index = originString.length

        originString.append(SPANNABLE_PREFIX)
        colorInformList.add(Triple(specialCharColor, index, originString.length))
        index = originString.length

        childMemoContentList.forEachIndexed { i, childContent ->
            originString.append("\t\t\t\t\t$childContent")
            colorInformList.add(Triple(subStrColor, index, originString.length))
            index = originString.length

            if (i != childContent.lastIndex) {
                originString.append(SPANNABLE_SPLIT)
                colorInformList.add(Triple(specialCharColor, index, originString.length))
                index = originString.length
            }
            originString.append('\n')
        }

        originString.append(SPANNABLE_SUFFIX)
        colorInformList.add(Triple(specialCharColor, index, originString.length))

        val spannableString = SpannableString(originString.toString())
        colorInformList.forEach {
            val color = ForegroundColorSpan(it.first)
            spannableString.setSpan(color, it.second, it.third, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        }

        return spannableString
    }

    fun getCreatedDate(context: Context) = String.format(context.getString(R.string.item_memo_created_date_format), StringUtils.getYearMonthDayStr(context, createdDate))
    fun getCompletedDate(context: Context) = String.format(context.getString(R.string.item_memo_completed_date_format), StringUtils.getYearMonthDayStr(context, completedDate!!))

}

fun Memo.boxing() = MemoBoxed(id, parentId, content, childMemoContentList, createdDate, completedDate)
fun MemoBoxed.unboxing() = Memo(id, parentId, content, childMemoContentList, createdDate, completedDate)