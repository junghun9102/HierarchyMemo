package com.yangdroid.hierarchymemo.extension

import android.content.Context
import com.yangdroid.hierarchymemo.ui.custom.CustomToast

fun Context.showToast(resId: Int) = CustomToast.show(this, resId)
fun Context.showErrorToast(resId: Int) = CustomToast.showError(this, resId)
fun Context.showSuccessToast(resId: Int) = CustomToast.showSuccess(this, resId)

fun Context.showErrorToast(message: String) = CustomToast.showError(this, message)