package com.example.foregroundservice

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var serviceIntent: Intent

    private lateinit var btnStartService: Button
    private lateinit var btnStopService: Button
    private lateinit var changeNotiBtn: Button

    private lateinit var requestNotificationPermission: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(this, SimpleForegroundService::class.java)

        btnStartService = findViewById(R.id.buttonStartService)
        btnStopService = findViewById(R.id.buttonStopService)
        changeNotiBtn = findViewById(R.id.changeNotiBtn)

        btnStartService.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (checkNotificationPermission()) {
                    SimpleForegroundService.startService(
                        this,
                        "Foreground Service Title",
                        "Foreground Service Content"
                    )
                } else {
                    requestNotificationPermission.launch(permission)

                }
            }
        }
        btnStopService.setOnClickListener { SimpleForegroundService.stopService(this) }

        changeNotiBtn.setOnClickListener{
            SimpleForegroundService.sendMessageToService(this, "new title","new content")
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