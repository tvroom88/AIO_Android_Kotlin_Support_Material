package com.example.anrtest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat

class ForegroundService : Service() {

    private var TAG = "SimpleForegroundService"

    private lateinit var notification: NotificationCompat.Builder
    private lateinit var manager: NotificationManager

    override fun onCreate() {
        // 서비스가 생성 될 때
        super.onCreate()
        Log.d(TAG, "ForegroundService - onCreate")
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d(TAG, "ForegroundService - onStartCommand")

        when (intent.action) {

            // startService()에 의해 서비스가 시작될 때
            "StartForeground" -> {
                Log.d(TAG, "ForegroundService - StartForeground")

                val inputTitle = intent.getStringExtra(Noti_Title)
                val inputContent = intent.getStringExtra(Noti_Content)

                //안드로이드 sdk 26버전 이상에서는 알림창을 띄워야 Foreground
                createNotificationChannel()

                // Notification 클릭시 MainActivity 이동
                val notificationIntent = Intent(this, MainActivity::class.java)
                val pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE)

                // 버튼 클릭시 Foreground Service 종료
                val stopServiceIntent = Intent(this, ForegroundService::class.java)
                stopServiceIntent.action = ("StopService")
                val stopPendingIntent = PendingIntent.getService(this, 0, stopServiceIntent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE)

                notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
                    setSmallIcon(R.drawable.ic_launcher_foreground)
                    setContentTitle(inputTitle)
                    setContentText(inputContent)
                    setStyle(NotificationCompat.BigTextStyle().bigText(inputContent))
                    addAction(R.drawable.ic_launcher_foreground, "stop service", stopPendingIntent)
                    setContentIntent(pendingIntent)
                }

                startForeground(1, notification.build())
            }


            "StopService" -> {
                Log.d(TAG, "ForegroundService - stopSelf")

                // 서비스를 정지시키는 로직을 구현합니다.
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }

        }

        return START_STICKY
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Foreground Service 예제" }
            manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d("ForegroundService", "ForegroundService - onBind")
        return null;
    }

    override fun onDestroy() {
        // 서비스가 종료할 때
        Log.d("ForegroundService", "ForegroundService - onDestroy")
    }

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"
        private const val Noti_Title = "Noti_Title"
        private const val Noti_Content = "Noti_Content"
    }

}