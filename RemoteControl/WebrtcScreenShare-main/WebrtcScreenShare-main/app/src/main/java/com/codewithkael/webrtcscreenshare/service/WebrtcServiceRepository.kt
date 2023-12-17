package com.codewithkael.webrtcscreenshare.service

import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.codewithkael.webrtcscreenshare.utils.DataModel
import com.codewithkael.webrtcscreenshare.utils.DataModelType
import javax.inject.Inject

class WebrtcServiceRepository @Inject constructor(
    private val context: Context
) {

    fun startIntent(username: String) {
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "StartIntent"
            startIntent.putExtra("username", username)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }

    fun requestConnection(target: String) {
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "RequestConnectionIntent"
            startIntent.putExtra("target", target)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }

    fun acceptCAll(target: String) {
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "AcceptCallIntent"
            startIntent.putExtra("target", target)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }

    fun endCallIntent() {
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "EndCallIntent"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }

    fun stopIntent() {
        val thread = Thread {

            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "StopIntent"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }


    // Callee ask to get remote control permission from caller
    fun sendRequestRemoteControlPermission(target: String) {
        Log.d("JaehongLee", "sendRequestRemoteControlPermission")

        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "RequestRemoteControlIntent"
            startIntent.putExtra("target", target)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }

    // status that caller accept or reject permission of remote control
    fun sendAccessibilityServiceStatusToServer(status: Boolean, target: String) {
        Log.d("goodgoodgood", "sendAccessibilityServiceStatusToServer")
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "accessibilityServiceStatusIntent"
            startIntent.putExtra("status", status)
            startIntent.putExtra("target", target)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }


    fun sendDataChannelMessage() {
        Log.d("abcabc", "WebrtcServiceRepository - sendDataChannelMessage")
        val thread = Thread {
            val startIntent = Intent(context, WebrtcService::class.java)
            startIntent.action = "dataChannelMessage"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(startIntent)
            } else {
                context.startService(startIntent)
            }
        }
        thread.start()
    }


    // AA(caller) BB(Callee)
    // BB -> ask remote permission to AA
    // AA -> Yes, -> BB
}
