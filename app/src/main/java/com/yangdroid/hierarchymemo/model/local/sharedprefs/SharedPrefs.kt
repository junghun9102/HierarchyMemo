package com.yangdroid.hierarchymemo.model.local.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import java.lang.Exception

class SharedPrefs private constructor(context: Context) {

    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var theme: String
        get() = prefs.getString(PREFS_THEME, "DEFAULT") ?: "DEFAULT"
        set(value) = prefs.edit().putString(PREFS_THEME, value).apply()

    companion object {
        const val PREFS_NAME = "prefs"
        const val PREFS_THEME = "theme"

        private var uniqueInstance: SharedPrefs? = null

        fun getInstance(): SharedPrefs {
            return uniqueInstance ?: throw Exception("You must init before call getInstance. It is recommended to initialize in Application's onCreate in advance.")
        }

        @Synchronized
        fun init(context: Context) {
            if (uniqueInstance == null) {
                uniqueInstance = SharedPrefs(context)
            }
        }
    }

}