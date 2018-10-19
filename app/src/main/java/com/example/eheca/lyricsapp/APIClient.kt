package com.example.eheca.lyricsapp

import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream

class `APIClient` {
    //Get the JSON Body as InputStream using the url of the api
    fun getJson(url: String): InputStream{
        val request = Request.Builder().url(url).build()
        val response = OkHttpClient().newCall(request).execute()
        val jsonBody = response.body()

        return  jsonBody!!.byteStream()
    }
}