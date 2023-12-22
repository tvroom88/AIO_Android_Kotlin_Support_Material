package com.example.backgroundservice

import android.app.Service
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import java.io.IOException


class BackgroundService : Service() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroudnService", "onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val mediaUrl = "https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3"
        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        try {
            mediaPlayer!!.setDataSource(mediaUrl)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackgroudnService", "onDestroy")
        mediaPlayer!!.stop()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}
