package com.example.eheca.lyricsapp

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDelegate
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.InputStreamReader

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
            var apiUrl = "https://api.lyrics.ovh/v1/artist/title"
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

            //Build the api url with artist and title parameters
            apiUrl = apiUrl!!.replace("artist", artist)
            apiUrl = apiUrl!!.replace("title", songName)
            //Execute ApiData
            GetApiData(this, apiUrl,songName).execute()
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
    private fun startLyricActivity(songName: String?, lyric: String?){
        //Starts LyricActivity again
        val intent = Intent(this, LyricActivity::class.java)
        intent.putExtra(LyricActivity.song_name_key,songName)
        intent.putExtra(LyricActivity.lyric_key, lyric)
        startActivity(intent)
    }
    private fun showErrorDialog(){
        //Build the dialog
        var dialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogBuilder.setTitle(R.string.dialog_title_error)
        dialogBuilder.setMessage(R.string.error_lyrics_not_found)
        dialogBuilder.setPositiveButton("Ok"){ _ , _ ->}

        //Create the dialog
        val dialog = dialogBuilder.create()
        //Display the dialog
        dialog.show()
    }
    //Async task to load the Api data
    open class GetApiData(mainActivity: MainActivity,apiUrl: String ,songName: String): AsyncTask<Unit, Unit, String>(){
        var mainActivity: MainActivity? = null
        var songName: String? = null
        var apiUrl: String ? = null
        init {
            this.mainActivity = mainActivity
            this.songName = songName
            this.apiUrl = apiUrl
        }

        override fun onPreExecute() {
            super.onPreExecute()
            //Show load lottie animation
            mainActivity?.search_Button?.visibility = View.GONE
            mainActivity?.loadView?.visibility = View.VISIBLE
        }
        override fun doInBackground(vararg p0: Unit?): String {
            //Start the ApiClient for search the lyrics song
            val apiClient = APIClient()
            val stream = BufferedInputStream(apiClient.getJson(apiUrl!!))
            return readStream(stream)
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            var jsonObject = JSONObject(result)
            //Hide load lottie animation
            mainActivity?.search_Button?.visibility = View.VISIBLE
            mainActivity?.loadView?.visibility = View.GONE
            //Show alert dialog if the lyric won´t be found
            if (jsonObject.isNull("lyrics")){
                mainActivity?.showErrorDialog()
                return
            }
            mainActivity?.startLyricActivity(songName, jsonObject.getString("lyrics"))
        }
        private fun readStream(inputStream: BufferedInputStream): String{
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val stringBuilder = StringBuilder()
            bufferedReader.forEachLine { stringBuilder.append(it) }
            return stringBuilder.toString()
        }
    }
}
