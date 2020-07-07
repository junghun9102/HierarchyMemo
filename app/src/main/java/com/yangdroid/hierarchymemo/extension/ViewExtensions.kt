package com.yangdroid.hierarchymemo.extension

import android.view.View

fun View.makeEnable() { this.isEnabled = true }
fun View.makeDisable() { this.isEnabled = false }

fun View.makeVisible() { this.visibility = View.VISIBLE }
fun View.makeInvisible() { this.visibility = View.INVISIBLE }
fun View.makeGone() { this.visibility = View.GONE}