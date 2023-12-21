package com.example.foregroundservice

import android.Manifest
import android.app.ActivityManager
import android.app.ActivityManager.AppTask
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var serviceIntent: Intent

    private lateinit var btnStartService: Button
    private lateinit var btnStopService: Button
    private lateinit var changeNotiBtn: Button
    private lateinit var checkRunningServiceBtn: Button
    private lateinit var startProgress: Button
    private lateinit var stopProgress: Button

    private lateinit var titleET: EditText
    private lateinit var contentET: EditText

    private lateinit var requestNotificationPermission: ActivityResultLauncher<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        serviceIntent = Intent(this, SimpleForegroundService::class.java)

        btnStartService = findViewById(R.id.buttonStartService)

        btnStopService = findViewById(R.id.buttonStopService)

        startProgress = findViewById(R.id.startProgress)

        stopProgress = findViewById(R.id.stopProgress)

        checkRunningServiceBtn = findViewById(R.id.checkRunningServiceBtn)

        changeNotiBtn = findViewById(R.id.changeNotiBtn)
        titleET = findViewById(R.id.titleET)
        contentET = findViewById(R.id.contentET)

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

        startProgress.setOnClickListener {
            if (isServiceRunning(this))
                SimpleForegroundService.startProgress(this)
            else
                Toast.makeText(this, "Foreground Service를 켜고 시도해주세요.", Toast.LENGTH_SHORT).show()

        }

        stopProgress.setOnClickListener {
            if (isServiceRunning(this))
                SimpleForegroundService.stopProgress(this)
            else
                Toast.makeText(this, "Foreground Service를 켜고 시도해주세요.", Toast.LENGTH_SHORT).show()
        }

        changeNotiBtn.setOnClickListener {
            if (isServiceRunning(this))
                SimpleForegroundService.sendMessageToService(
                    this,
                    titleET.text.toString(),
                    contentET.text.toString()
                )
            else
                Toast.makeText(this, "Foreground Service가 켜져있지 않습니다.", Toast.LENGTH_SHORT).show()
        }

        checkRunningServiceBtn.setOnClickListener {
            if (isServiceRunning(this))
                Toast.makeText(this, "Foreground Service가 켜져있습니다.", Toast.LENGTH_SHORT).show()
            else
                Toast.makeText(this, "Foreground Service가 켜져있지 않습니다.", Toast.LENGTH_SHORT).show()
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

    private fun isServiceRunning(context: Context): Boolean {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (rsi in am.getRunningServices(Integer.MAX_VALUE)) {
            if (SimpleForegroundService::class.java.name == rsi.service.className) {
                return true
            }
        }
        return false
    }


    companion object {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        val permission = Manifest.permission.POST_NOTIFICATIONS
    }
}