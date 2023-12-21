package com.example.foregroundservice

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
    private var binder: IBinder? = null        // bind 된 클라이언트와 소통하기 위한 인터페이스

    private lateinit var notification: NotificationCompat.Builder
    private lateinit var manager: NotificationManager

    private var progressThread: Thread? = null
    private var startFlag = true
    private var threadIsRunning = true // Thread가 여려개 켲지는 것을 막기 위해서 


    var startProgressPoint = 0

    override fun onCreate() {
        // 서비스가 생성 될 때
        super.onCreate()
        Log.d(TAG, "ForegroundService - onCreate")

//        manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
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

                    // Notification 클릭시 MainActivity 이동
                    val notificationIntent = Intent(this, MainActivity::class.java)
                    val pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_IMMUTABLE
                    )

                    //  Notification 클릭시 SimpleForegroundService 이동. Progress Bar 작동 시작
                    val startProgressIntent = Intent(this, SimpleForegroundService::class.java)
                    startProgressIntent.action = ("StartProgress")
                    val startProgressPendingIntent = PendingIntent.getService(
                        this,
                        0,
                        startProgressIntent,
                        PendingIntent.FLAG_MUTABLE
                    )

                    // Progress 정지
                    val stopProgressIntent = Intent(this, SimpleForegroundService::class.java)
                    stopProgressIntent.action = ("StopProgress")
                    val stopProgressPendingIntent = PendingIntent.getService(
                        this,
                        0,
                        stopProgressIntent,
                        PendingIntent.FLAG_MUTABLE
                    )


                    // 버튼 클릭시 Foreground Service 종료
                    val stopServiceIntent = Intent(this, SimpleForegroundService::class.java)
                    stopServiceIntent.action = ("StopService")
                    val stopPendingIntent = PendingIntent.getService(
                        this,
                        0,
                        stopServiceIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_MUTABLE
                    )


                    notification = NotificationCompat.Builder(this, CHANNEL_ID).apply {
                        setSmallIcon(R.drawable.coffee_icon)
                        setContentTitle(inputTitle)
                        setContentText(inputContent)
                        setProgress(100, 0, false)
                        setStyle(
                            NotificationCompat.BigTextStyle()
                                .bigText(inputContent)
                        )
                        addAction(R.drawable.start, "start progress", startProgressPendingIntent)
                        addAction(R.drawable.cancel, "stop progress", stopProgressPendingIntent)
                        addAction(R.drawable.cancel, "stop service", stopPendingIntent)
                        setContentIntent(pendingIntent)
                    }

                    startForeground(1, notification.build())

                }

                // Progess 시작
                "StartProgress" -> {
                    Log.d(TAG, "ForegroundService - StartProgress")
                    startProgress()
                }

                // Progress 정지
                "StopProgress" -> {
                    Log.d(TAG, "ForegroundService - StopProgress")
                    stopProgress()
                }

                "StopService" -> {
                    Log.d(TAG, "ForegroundService - stopSelf")

                    // 서비스를 정지시키는 로직을 구현합니다.
                    stopForeground(STOP_FOREGROUND_REMOVE)
                    stopSelf()
                }

                // Notification 제목과 내용 바꿀때
                "getMessageFromActivity" -> {
                    val inputTitle = intent.getStringExtra(Noti_Title)
                    val inputContent = intent.getStringExtra(Noti_Content)
                    notification.setContentTitle(inputTitle)
                    notification.setContentText(inputContent)
                    notification.setStyle(NotificationCompat.BigTextStyle().bigText(inputContent))
                    manager.notify(1, notification.build())
                }

            }
        }

        return START_STICKY
    }

    private fun startProgress() {

        progressThread = Thread {
            for (incr in startProgressPoint..100 step 5) {
                if (startFlag) {
                    notification.setProgress(100, incr, false)
                    startProgressPoint = incr
                    manager.notify(1, notification.build())
                    try {
                        Thread.sleep(1 * 500.toLong())
                    } catch (e: InterruptedException) {
                        Log.d("TAG", "sleep failure")
                    }
                } else {
                    startFlag = true
                    break
                }
            }
            // When the loop is finished, updates the notification
            notification.setContentText("Download completed")
            manager.notify(1, notification.build())
        }
        if (threadIsRunning) { // Thread가 한번 켜지면 더 실행되지 않게 하기 위해서
            progressThread!!.start()
            threadIsRunning = false
        }

    }

    private fun stopProgress() {
        if (startFlag && progressThread != null) {
            startFlag = false
            threadIsRunning = true // Thread가 정지 되면 다시 Thread가 실행되도 되게
            progressThread!!.interrupt()
            progressThread = null
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply { description = "Foreground Service 예제" }
            manager = getSystemService(
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


        fun startProgress(context: Context) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            intent.action = "StartProgress"
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

        fun stopProgress(context: Context) {
            val intent = Intent(context, SimpleForegroundService::class.java)
            intent.action = "StopProgress"
            if (Build.VERSION.SDK_INT < 26) {
                context.startService(intent)
            } else {
                context.startForegroundService(intent)
            }
        }

    }


    override fun onBind(intent: Intent): IBinder? {
        // bindService()에 의해 서비스가 시작될 때
        Log.d("ForegroundService", "ForegroundService - onBind")
        return binder
    }

    override fun onDestroy() {
        // 서비스가 종료할 때
        Log.d("ForegroundService", "ForegroundService - onDestroy")

    }

}