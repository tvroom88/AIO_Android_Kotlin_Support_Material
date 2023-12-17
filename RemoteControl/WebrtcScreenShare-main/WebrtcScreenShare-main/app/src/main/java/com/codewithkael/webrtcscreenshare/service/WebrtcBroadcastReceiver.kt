package com.codewithkael.webrtcscreenshare.service

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout

class WebrtcBroadcastReceiver(private val service: AccessibilityService) :
    BroadcastReceiver() {
    var wm: WindowManager? = null
    var mLayout: FrameLayout? = null
    var params: WindowManager.LayoutParams? = null
    var laserIsShowing = false


    companion object {
        private val TAG = WebrtcBroadcastReceiver::class.java.simpleName
    }

    override fun onReceive(p0: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "startBroadcastReceiver") {
                Log.d("JaehongJaehong", "Broadcast Receiver is start")
            }

            if(intent.action == "hi"){
                Log.d("abcabc", "Broadcast Receiver is hi")

            }
        }

    }
}