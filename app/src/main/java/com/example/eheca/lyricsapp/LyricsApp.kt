package com.example.eheca.lyricsapp

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatDelegate

class LyricsApp: Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(getAppDayNightMode())
    }
    private fun getAppDayNightMode(): Int{
        var dayNightMode: Int
        //Get the Shared Preferences File
        val sp = getSharedPreferences(getString(R.string.shared_preferences_app_options_file), Context.MODE_PRIVATE)
        //Get the value stored in Shared Preferences
        dayNightMode = sp.getInt(getString(R.string.shared_preferences_app_day_night_mode), AppCompatDelegate.MODE_NIGHT_YES)

        return dayNightMode
    }
    fun getAppTheme(): Int{
        var appTheme: Int
        //Get the Shared Preferences File
        val sp = getSharedPreferences(getString(R.string.shared_preferences_app_options_file), Context.MODE_PRIVATE)
        //Get the value stored in Shared Preferences
        appTheme = sp.getInt(getString(R.string.shared_preferences_app_theme), R.style.DarkTheme)

        return appTheme
    }

}