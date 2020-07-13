package com.yangdroid.hierarchymemo.ui.custom

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.yangdroid.hierarchymemo.R
import kotlinx.android.synthetic.main.view_toast.view.*
import org.jetbrains.anko.dimen

object CustomToast {

    fun show(context: Context, stringResId: Int, duration: Int = Toast.LENGTH_SHORT) = showToast(context, context.getString(stringResId), duration)
    fun showError(context: Context, stringResId: Int, duration: Int = Toast.LENGTH_SHORT) = showToast(context, context.getString(stringResId), duration, R.color.colorErrorMessage)
    fun showSuccess(context: Context, stringResId: Int, duration: Int = Toast.LENGTH_SHORT) = showToast(context, context.getString(stringResId), duration, R.color.colorSystemMessage)

    fun showError(context: Context, message: String, duration: Int = Toast.LENGTH_SHORT) = showToast(context, message, duration, R.color.colorErrorMessage)

    private fun showToast(context: Context, content: String, duration: Int, colorResId: Int? = null) {
        val view = LayoutInflater.from(context).inflate(R.layout.view_toast, null)
        view.tv_view_toast.text = content
        colorResId?.let {
            view.tv_view_toast.setTextColor(ContextCompat.getColor(context, it))
        }

        Toast(context).apply {
            setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, context.dimen(R.dimen.custom_toast_margin_bottom))
            this.duration = duration
            setView(view)
        }.show()
    }

}