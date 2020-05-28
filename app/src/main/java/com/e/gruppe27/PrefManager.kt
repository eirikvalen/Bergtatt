package com.e.gruppe27

import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor

private const val PRIVATE_MODE = 0
private const val PREF_NAME = "androidhive-welcome"
private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"

class PrefManager {

    private lateinit var pref: SharedPreferences
    private lateinit var editor: Editor
    private lateinit var _context: Context


    fun prefManager(context: Context) {
        _context = context
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
        editor.apply()
    }

    //Setter status på landingpage vist tidligere
    fun setFirstTimeLaunch(isFirstTime: Boolean) {
        editor.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime)
        editor.commit()
    }

    fun isFirstTimeLaunch(): Boolean {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, false)
    }


}