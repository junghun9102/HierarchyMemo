package com.yangdroid.hierarchymemo.ui.theme_setting

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.yangdroid.hierarchymemo.R
import com.yangdroid.hierarchymemo.databinding.ActivityThemeSettingBinding
import com.yangdroid.hierarchymemo.extension.makeGone
import com.yangdroid.hierarchymemo.extension.makeInvisible
import com.yangdroid.hierarchymemo.model.local.sharedprefs.SharedPrefs
import com.yangdroid.hierarchymemo.ui.model.MemoBoxed
import kotlinx.android.synthetic.main.activity_theme_setting.*
import kotlinx.android.synthetic.main.item_memo.view.*
import java.util.*

class ThemeSettingActivity : AppCompatActivity(), ThemeSettingContract.View {

    private val presenter: ThemeSettingPresenter = ThemeSettingPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataBindingUtil.setContentView<ActivityThemeSettingBinding>(this, R.layout.activity_theme_setting).apply {
            presenter = this@ThemeSettingActivity.presenter
            lifecycleOwner = this@ThemeSettingActivity
        }

        initViews()
        ThemeSettingPresenter.Theme.valueOf(SharedPrefs.getInstance().theme).let {
            presenter.onCreate(it)
        }
    }

    private fun initViews() {
        initSampleMemo()
    }

    private fun initSampleMemo() {
        initSampleMemo(layout_theme_setting_dark_memo)
        initSampleMemo(layout_theme_setting_light_memo)
    }

    private fun initSampleMemo(view: View) {
        view.run {
            this.cl_item_memo_expand_minus.makeInvisible()
            this.iv_item_memo_expand_plus.makeInvisible()
            this.ll_item_memo_edit_inform.makeGone()
            this.isClickable = false
        }

        if (view == layout_theme_setting_dark_memo) {
            initSampleMemoDark(view)
        } else {
            initSampleMemoLight(view)
        }
    }

    private fun initSampleMemoLight(view: View) {
        val mainColor = ContextCompat.getColor(this, R.color.colorLightMemoMain)
        val subColor = ContextCompat.getColor(this, R.color.colorLightMemoSub)
        val specialColor = ContextCompat.getColor(this, R.color.colorLightMemoSpecial)
        view.tv_item_memo_content.text = sampleMemo.getSpannableMemo(this, mainColor, specialColor)
        view.tv_item_memo_expand_content_child.text = sampleMemo.getSpannableChildMemo(this, subColor, specialColor)
    }

    private fun initSampleMemoDark(view: View) {
        val mainColor = ContextCompat.getColor(this, R.color.colorDarkMemoMain)
        val subColor = ContextCompat.getColor(this, R.color.colorDarkMemoSub)
        val specialColor = ContextCompat.getColor(this, R.color.colorDarkMemoSpecial)
        view.tv_item_memo_content.text = sampleMemo.getSpannableMemo(this, mainColor, specialColor)
        view.tv_item_memo_expand_content_child.text = sampleMemo.getSpannableChildMemo(this, subColor, specialColor)
    }

    override fun updateConfigureToDefaultTheme() {
        Handler().postDelayed({
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            SharedPrefs.getInstance().theme = ThemeSettingPresenter.Theme.DEFAULT.name
        }, DELAY_FOR_UI_CHANGE)
    }

    override fun updateConfigureToLightTheme() {
        Handler().postDelayed({
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            SharedPrefs.getInstance().theme = ThemeSettingPresenter.Theme.LIGHT.name
        }, DELAY_FOR_UI_CHANGE)
    }

    override fun updateConfigureToDarkTheme() {
        Handler().postDelayed({
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            SharedPrefs.getInstance().theme = ThemeSettingPresenter.Theme.DARK.name
        }, DELAY_FOR_UI_CHANGE)
    }

    private val sampleChildContentList = listOf("Theme", "Setting", "Sample")
    private val sampleMemo = MemoBoxed(null, null, "Theme Setting Sample", sampleChildContentList,  Date(), null, false)

    companion object {
        // Data binding is not working properly. It does not apply to parts. So we need a delay to change the UI
        const val DELAY_FOR_UI_CHANGE = 500L
    }

}
