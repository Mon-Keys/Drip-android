package com.drip.dripapplication.data.utils

import android.content.Context
import android.content.SharedPreferences

object SharedPrefs {

    private var sharedPrefs: SharedPreferences? = null

    var authToken: String?
        get() = sharedPrefs?.getString(AUTH_TOKEN, null)
        set(value) {
            sharedPrefs?.edit()
                ?.putString(AUTH_TOKEN, value)
                ?.apply()
        }

    fun init(context: Context){
        sharedPrefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    }
    private const val NAME = "app_preferences"
    private const val AUTH_TOKEN = "auth_token"
}