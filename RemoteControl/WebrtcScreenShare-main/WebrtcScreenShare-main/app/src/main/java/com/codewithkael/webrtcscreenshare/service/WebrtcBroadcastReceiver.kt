package com.codewithkael.webrtcscreenshare.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityService.GestureResultCallback
import android.accessibilityservice.GestureDescription
import android.accessibilityservice.GestureDescription.StrokeDescription
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Path
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

        Log.d("WebrtcBroadcastReceiver", "Broadcast Receiver is start")


        if (intent != null) {
            if (intent.action == "startBroadcastReceiver") {
                Log.d("WebrtcBroadcastReceiver", "Broadcast Receiver is start")
                WebrtcAccessibilityService.isBroadCastReceiverRegistered = true
            }

            if (intent.action == "hi") {
                Log.d("WebrtcBroadcastReceiver", "Broadcast Receiver is hi")
            }

            if (intent.action == "sendXandYCoordination") {

                val x = intent.getFloatExtra("xRatio", 0F)
                val y = intent.getFloatExtra("yRatio", 0F)

                Log.d("WebrtcBroadcastReceiver", "x " + x)
                Log.d("WebrtcBroadcastReceiver", "y " + y)

                tap(x, y)
            }
        }

    }

    private fun tap(x: Float, y: Float) {
        Log.d("onReceive", "tap-method")
        val path = Path()
        path.moveTo(x, y)
        val stroke = StrokeDescription(path, 0, 100)
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(stroke)
        val gesture = gestureBuilder.build()

        service.dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription) {
                super.onCompleted(gestureDescription)
                Log.d("abcabc", "Gesture completed")
            }

            override fun onCancelled(gestureDescription: GestureDescription) {
                super.onCancelled(gestureDescription)
                Log.d("abcabc", "Gesture cancelled")
            }
        }, null)


    }

}