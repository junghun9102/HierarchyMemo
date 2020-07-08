package com.yangdroid.hierarchymemo.component

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.ViewTreeObserver
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LiveData
import com.yangdroid.hierarchymemo.extension.observeNotNull
import com.yangdroid.hierarchymemo.extension.plusAssign
import com.yangdroid.hierarchymemo.utils.AutoClearedDisposable
import dagger.android.support.DaggerAppCompatActivity

abstract class BaseActivity : DaggerAppCompatActivity() {

    lateinit var disposables: AutoClearedDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDisposable()
    }

    private fun initDisposable() {
        disposables = AutoClearedDisposable(this)
        lifecycle += disposables
    }

    protected fun initActionbar(
        toolbar: Toolbar,
        titleColor: Int = Color.WHITE,
        isEnableNavigation: Boolean = true,
        onClickNavigation: () -> Unit = {}
    ) {
        toolbar.setTitleTextColor(titleColor)
        setSupportActionBar(toolbar)
        if (isEnableNavigation) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            toolbar.setNavigationOnClickListener {
                onClickNavigation.invoke()
            }
        }
    }

    var isShowKeyboard: Boolean = false

    private lateinit var onGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener

    fun addKeyboardListener(onShowKeyboard: () -> Unit = {}, onHideKeyBoard: () -> Unit = {}) {
        val minKeyboardHeight = 150
        val decorView = window.decorView
        onGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {
            private val windowVisibleDisplayFrame = Rect()
            private var lastVisibleDecorViewHeight: Int = 0

            override fun onGlobalLayout() {
                decorView.getWindowVisibleDisplayFrame(windowVisibleDisplayFrame)
                val visibleDecorViewHeight = windowVisibleDisplayFrame.height()
                if (lastVisibleDecorViewHeight != 0) {
                    if (lastVisibleDecorViewHeight + minKeyboardHeight < visibleDecorViewHeight) {
                        onHideKeyBoard.invoke()
                    }
                } else {
                    if ((lastVisibleDecorViewHeight > visibleDecorViewHeight + minKeyboardHeight) ||
                        (isShowKeyboard && lastVisibleDecorViewHeight != visibleDecorViewHeight)) {
                        onShowKeyboard.invoke()
                        isShowKeyboard = true
                    }
                }
                lastVisibleDecorViewHeight = visibleDecorViewHeight
            }
        }
        decorView.viewTreeObserver.addOnGlobalLayoutListener(onGlobalLayoutListener)
    }

    fun removeKeyboardListener() {
        val decorView = window.decorView
        decorView.viewTreeObserver.removeOnGlobalLayoutListener(onGlobalLayoutListener)
    }


    protected fun <T> LiveData<T>.observe(observer: (T) -> Unit) = observeNotNull(this@BaseActivity, observer)

}