package com.example.anrtest

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
class BackgroundService : Service() {

    override fun onCreate() {
        super.onCreate()
        Log.d("BackgroundService", "onCreate")

        plus()
        plus()
        plus()
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("BackgroundService", "onStartCommand")
        Toast.makeText(this, "called onStartCommand", Toast.LENGTH_SHORT).show()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("BackgroundService", "onDestroy")
        Toast.makeText(this, "called onDestroy", Toast.LENGTH_SHORT).show()
    }

    @Nullable
    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun plus(): String {
        var number = 0
        for (i in Int.MIN_VALUE + 0..Int.MAX_VALUE) {
            number = i
        }
        return number.toString()
    }
}