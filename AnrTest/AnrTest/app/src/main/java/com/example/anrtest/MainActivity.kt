package com.example.anrtest

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import android.Manifest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts


class MainActivity : AppCompatActivity() {

    private lateinit var requestNotificationPermission: ActivityResultLauncher<String>

    private lateinit var serviceIntent: Intent

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val countBtn = findViewById<Button>(R.id.countBtn)

        // 1. 입력 전달 타임아웃
        countBtn.setOnClickListener {
            plus()
        }

        // 2. background service 실행
        val startServiceBtn = findViewById<Button>(R.id.startServiceBtn)
        startServiceBtn.setOnClickListener {
            startService(Intent(this@MainActivity, BackgroundService::class.java))
        }

        val stopServiceBtn = findViewById<Button>(R.id.stopServiceBtn)
        stopServiceBtn.setOnClickListener {
            stopService(Intent(this@MainActivity, BackgroundService::class.java))
        }

        // 3. foreground service 실행
        serviceIntent = Intent(this, ForegroundService::class.java)
//        serviceIntent.action = "StartForeground"

        val startForegroundServiceBtn = findViewById<Button>(R.id.startForegroundServiceBtn)
        startForegroundServiceBtn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkNotificationPermission()) {
                    startForegroundService(serviceIntent)
                } else {
                    requestNotificationPermission.launch(permission)
                }
            }else{
                startForegroundService(serviceIntent)
            }
        }

        val stopForegroundServiceBtn = findViewById<Button>(R.id.stopForegroundServiceBtn)
        stopForegroundServiceBtn.setOnClickListener {
            stopService(serviceIntent)
        }


        requestNotificationPermission =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        startForegroundService(serviceIntent)
                    } else {
                        startService(serviceIntent)
                    }
                } else {
                    Toast.makeText(
                        applicationContext,
                        "Notification permission을 승낙해주세요..",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    // 1. 입력 전달 타임아웃
    fun plus(): String {
        var number = 0
        for (i in Int.MIN_VALUE + 0..Int.MAX_VALUE) {
            number = i
        }
        return number.toString()
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    fun checkNotificationPermission(): Boolean {
        return when (PackageManager.PERMISSION_GRANTED) {
            ContextCompat.checkSelfPermission(this, permission) -> {
                true
            }

            else -> {
                false
            }
        }
    }

    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val permission = Manifest.permission.POST_NOTIFICATIONS
    }
}