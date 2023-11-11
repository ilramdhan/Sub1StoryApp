package com.dicoding.sub1storyapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.dicoding.sub1storyapp.utils.ValManager.KEY_EMAIL
import com.dicoding.sub1storyapp.utils.ValManager.KEY_ISLOGIN
import com.dicoding.sub1storyapp.utils.ValManager.KEY_TOKEN
import com.dicoding.sub1storyapp.utils.ValManager.KEY_USER_NAME
import com.dicoding.sub1storyapp.utils.ValManager.PREF_NAME

class Session(context: Context) {

    private var pref: SharedPreferences = context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val edit = pref.edit()

//    fun setIntPreference(prefKey: String, value: Int) {
//        edit.putInt(prefKey, value)
//        edit.apply()
//    }

    fun setStringPreference(prefKey: String, value: String) {
        edit.putString(prefKey, value)
        edit.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        edit.putBoolean(prefKey, value)
        edit.apply()
    }

//    fun clearPreferenceByKey(prefKey: String) {
//        edit.remove(prefKey)
//        edit.apply()
//    }

    fun clearPreferences() {
        edit.clear().apply()
    }

    val getToken = pref.getString(KEY_TOKEN, "")
//    val getUserId = pref.getString(KEY_USER_ID, "")
    val getUserName = pref.getString(KEY_USER_NAME, "")
    val getEmail = pref.getString(KEY_EMAIL, "")
    val isLogin = pref.getBoolean(KEY_ISLOGIN, false)
}