package com.example.eheca.lyricsapp

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AppCompatDelegate
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity() {
    private var lyricsApp: LyricsApp? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        //Get the LyricsApp class
        lyricsApp = application as LyricsApp
        //Set the Light Dark Theme
        setTheme(lyricsApp!!.getAppTheme())

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        //Verify if the darkMode is activated to change the nightMode_Switch in checked or unchecked
        nightMode_Switch.isChecked = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
    }

    override fun onStart() {
        super.onStart()
        //nightMode_Switch Listener
        nightMode_Switch.setOnCheckedChangeListener { _, isSelected ->
            val sp = getSharedPreferences(getString(R.string.shared_preferences_app_options_file), Context.MODE_PRIVATE)
            var dayNightMode: Int
            var appTheme: Int

            if  (isSelected){
                dayNightMode = AppCompatDelegate.MODE_NIGHT_YES
                appTheme = R.style.DarkTheme
            }else{
                dayNightMode = AppCompatDelegate.MODE_NIGHT_NO
                appTheme = R.style.LightTheme
            }
            with(sp.edit()){
                putInt(getString(R.string.shared_preferences_app_day_night_mode), dayNightMode)
                putInt(getString(R.string.shared_preferences_app_theme), appTheme)
                commit()
            }
            //Set the new DayNightMode
            AppCompatDelegate.setDefaultNightMode(dayNightMode)
            //Restart Application
            restartApp()
        }
        //search_Button Listener
        search_Button.setOnClickListener {
            //Get the artist and song name
            val artist: String = artist_EditText.text.toString()
            val songName: String = songName_EditText.text.toString()

            //return if artist is empty
            if  (artist.isEmpty()){
                artist_EditText.error = getString(R.string.error_message_empty_artist)
                return@setOnClickListener
            }else
                artist_EditText.error = null

            //return if song name is empty
            if  (songName.isEmpty()){
                songName_EditText.error = getString(R.string.error_message_empty_song_name)
                return@setOnClickListener
            }else
                songName_EditText.error = null

            //Get the lyric
            val lyric = "Antes de que nos olviden\r\nHaremos historia.\r\nNo andaremos de rodillas;\r\n\nEl alma no tiene la culpa. \nAntes de que nos olviden\n\nRasgaremos paredes\n\nY buscaremos restos;\n\nNo importa si fue nuestra vida. \n\n\n\nAntes de que nos olviden\n\nNos evaporaremos en magueyes,\n\nY subiremos hasta el cielo \n\n\n\nY bajaremos con la lluvia.\n\nAntes de que nos olviden\n\nRomperemos jaulas,\n\nY gritaremos la fuga;\n\nNo hay que condenar el alma. \n\n\n\nAunque tu me olvides,\n\nTe pondré en un altar de veladoras,\n\nY en cada una pondré tu nombre,\n\nY cuidare de tu alma. \n\n\n\nAmén."

            startLyricActivity(songName = songName, lyric = lyric)
        }
    }
    private fun restartApp(){
        //Starts MainActivity again
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        //Finish the application
        finish()
    }
    private fun startLyricActivity(songName: String, lyric: String){
        //Starts LyricActivity again
        val intent = Intent(this, LyricActivity::class.java)
        intent.putExtra(LyricActivity.song_name_key,songName)
        intent.putExtra(LyricActivity.lyric_key, lyric)
        startActivity(intent)
    }
}
