package com.example.uber.stepikapp1

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.IBinder
import io.reactivex.Observable
import java.net.HttpURLConnection
import java.net.URL

fun createRequest(url: String) =
        Observable.create<String> {
        val urlConnection = URL(url).openConnection() as HttpURLConnection
        try {
            urlConnection.connect()

            if (urlConnection.responseCode != HttpURLConnection.HTTP_OK)
                it.onError(RuntimeException(urlConnection.responseMessage))
            else {
                val str = urlConnection.inputStream.bufferedReader().readText()
                it.onNext(str)
            }
        } finally {
            urlConnection.disconnect()
        }

    }

class PlayService: Service() {


    var player: MediaPlayer?=null

    override fun onCreate() {
        super.onCreate()
    }

    override fun onDestroy() {
        player?.stop()
        super.onDestroy()
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        player?.stop()

        val url=intent!!.extras.getString("mp3")
        player = MediaPlayer()
        player?.setDataSource(this, Uri.parse(url))

        player?.setOnPreparedListener{p ->
            p.start()
        }

        player?.prepareAsync()

        return START_NOT_STICKY
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


}