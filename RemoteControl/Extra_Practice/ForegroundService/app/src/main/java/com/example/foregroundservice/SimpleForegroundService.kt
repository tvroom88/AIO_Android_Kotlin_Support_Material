package com.example.foregroundservice

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat


class SimpleForegroundService : Service() {


    private var TAG = "SimpleForegroundService"
    private var startMode: Int = 0             // 서비스가 kill 될 때, 어떻게 동작할지를 나타냄
    private var binder: IBinder? = null        // bind 된 클라이언트와 소통하기 위한 인터페이스
    private var allowRebind: Boolean = false   // onRebind() 메소드가 사용될지 말지를 결정함
    private lateinit var notification: Notification
    override fun onCreate() {
        // 서비스가 생성 될 때
        Log.d(TAG, "ForegroundService - onCreate")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        Log.d(TAG, "ForegroundService - onStartCommand")

        if (intent != null) {

            when (intent.action) {

                // startService()에 의해 서비스가 시작될 때
                "StartForeground" -> {
                    Log.d(TAG, "ForegroundService - StartForeground")

                    val inputTitle = intent.getStringExtra(Noti_Title)
                    val inputContent = intent.getStringExtra(Noti_Content)

                    //안드로이드 sdk 26버전 이상에서는 알림창을 띄워야 Foreground
                    createNotificationChannel()

                    if (inputTitle != null && inputContent != null) {
                        startNewService(inputTitle, inputContent)
                    }
                }

                "StopService" -> {
                    Log.d(TAG, "ForegroundService - stopSelf")

                    // 서비스를 정지시키는 로직을 구현합니다.
                    stopForeground(STOP_FOREGROUND_DETACH)
                    stopSelf()
                }

                "getMessageFromActivity" -> {
                    
                    val inputTitle = intent.getStringExtra(Noti_Title)
                    val inputContent = intent.getStringExtra(Noti_Content)

                    if (inputTitle != null && inputContent != null) {
                        startNewService(inputTitle, inputContent)
                    }
                }
            }
        }

        return START_STICKY
    }

    private fun startNewService(inputTitle: String, inputContent: String) {
        // Notification 클릭시 MainActivity 이동
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        // 버튼 클릭시 Foreground Service 종료
        val stopServiceIntent = Intent(this, SimpleForegroundService::class.java)
        stopServiceIntent.action = ("StopService")
        val stopPendingIntent = PendingIntent.getService(
            this,
            0,
            stopServiceIntent,
            PendingIntent.FLAG_MUTABLE
        )


        notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(inputTitle)
            .setContentText(inputContent)
            .setSmallIcon(R.drawable.coffee_icon)
            .addAction(R.drawable.cancel, "stop service", stopPendingIntent)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent): IBinder? {
        // bindService()에 의해 서비스가 시작될 때
        Log.d("ForegroundService", "ForegroundService - onBind")
        return binder
    }

    override fun onUnbind(intent: Intent): Boolean {
        // 모든 클라이언트가 unbindService()를 호출했을 때. 즉, 더이상 바인딩된 클라이언트가 없을때
        Log.d("ForegroundService", "ForegroundService - onUnbind")
        return allowRebind
    }

    override fun onRebind(intent: Intent) {
        // onUnbind()가 호출된적이 있는 상태에서, 다시 bindService()를 통해 바인딩 할 때
        Log.d("ForegroundService", "ForegroundService - onRebind")
    }

    override fun onDestroy() {
        // 서비스가 종료할 때
        Log.d("ForegroundService", "ForegroundService - onDestroy")

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

    companion object {
        const val CHANNEL_ID = "ForegroundServiceChannel"

        private const val Noti_Title = "Noti_Title"
        private const val Noti_Content = "Noti_Content"
        fun startService(context: Context, title: String, content: String) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            intent.action = "StartForeground"
            intent.putExtra(Noti_Title, title)
            intent.putExtra(Noti_Content, content)
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun sendMessageToService(context: Context, title: String, content: String) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            intent.action = "getMessageFromActivity"
            intent.putExtra(Noti_Title, title)
            intent.putExtra(Noti_Content, content)
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun stopService(context: Context) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            context.stopService(intent)
        }
    }
}