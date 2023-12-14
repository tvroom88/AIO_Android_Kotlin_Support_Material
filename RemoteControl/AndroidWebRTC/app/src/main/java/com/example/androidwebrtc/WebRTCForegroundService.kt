package com.example.androidwebrtc

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class WebRTCForegroundService : Service() {

    private val NOTIFICATION_ID = 1
    private val CHANNEL_ID = "ForegroundServiceChannel"
    private val binder: IBinder = MyBinder()

    private var isServiceRunning = false
    private lateinit var notificationManager: NotificationManager
    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(NotificationManager::class.java)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("onStartCommand", "coming here")
//        createNotificationChannel()

        intent?.let { incomingIntent ->
            when (incomingIntent.action) {
                "START_SERVICE" -> startWebRTCForegroundService(incomingIntent)

                else -> Unit
            }
        }

        if (intent == null) {
            Log.d("onStartCommand", "intent is null")
        }
        if (intent != null) {
            Log.d("onStartCommand", "intent.action : " + intent!!.action)
            if (intent.action == null) {
                Log.d("onStartCommand", "intent action is null")
            }
        }
        return START_STICKY
    }


    private fun startWebRTCForegroundService(incomingIntent: Intent) {
        //start our foreground service
        if (!isServiceRunning) {
            isServiceRunning = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val notificationChannel = NotificationChannel(
                    "channel1", "foreground", NotificationManager.IMPORTANCE_HIGH
                )

                val intent = Intent(this, WebRTCForegroundService::class.java).apply { action = "ACTION_EXIT" }
                val pendingIntent : PendingIntent = PendingIntent.getBroadcast(this,0 ,intent,PendingIntent.FLAG_IMMUTABLE)

                notificationManager.createNotificationChannel(notificationChannel)
                val notification = NotificationCompat.Builder(this, "channel1")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .addAction(R.drawable.ic_end_call,"Exit",pendingIntent)

                startForeground(1, notification.build())
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }


    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    inner class MyBinder : Binder() {
        fun getService(): WebRTCForegroundService {
            return this@WebRTCForegroundService
        }
    }
}