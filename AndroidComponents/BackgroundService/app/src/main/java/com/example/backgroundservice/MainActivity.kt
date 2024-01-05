package com.example.backgroundservice

import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.IOException


class MainActivity : AppCompatActivity() {

    private var startBtn: Button? = null
    private var stopBtn: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.idBtnStartService)
        stopBtn = findViewById(R.id.idBtnStopService)

        startBtn?.setOnClickListener {
            startService(Intent(this@MainActivity, BackgroundService::class.java))
            Toast.makeText(this@MainActivity, "Audio started playing..", Toast.LENGTH_SHORT).show()
        }

        stopBtn?.setOnClickListener {
            stopService(Intent(this@MainActivity, BackgroundService::class.java))
            Toast.makeText(this@MainActivity, "Audio stopped playing..", Toast.LENGTH_SHORT).show()
        }

    }
}