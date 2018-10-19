package com.example.eheca.lyricsapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_lyric.*
import kotlinx.android.synthetic.main.app_bar_main.*

class LyricActivity : AppCompatActivity() {
    private var lyricsApp: LyricsApp? = null
    companion object {
        const val song_name_key : String = "SONG_NAME_KEY"
        const val lyric_key = "LYRIC_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //Get the LyricsApp class
        lyricsApp = application as LyricsApp
        //Set the Light Dark Theme
        setTheme(lyricsApp!!.getAppTheme())

        //Set the back arrow in toolbar
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lyric)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)

        //Get the lyrics from intent
        if (intent != null){
            val bundle: Bundle = intent.extras
            //Get the lyric and the song name from intent
            val lyrics: String = bundle.getString(lyric_key)
            val songName: String = bundle.getString(song_name_key)

            //Set the title in toolbar
            supportActionBar?.title = songName

            lyric_TextView.text = lyrics
        }
    }

    override fun onBackPressed() {
        onSupportNavigateUp()
    }
}
