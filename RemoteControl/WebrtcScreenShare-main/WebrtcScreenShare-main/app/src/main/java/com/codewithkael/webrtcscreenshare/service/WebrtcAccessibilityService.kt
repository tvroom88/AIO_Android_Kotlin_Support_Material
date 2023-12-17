package com.codewithkael.webrtcscreenshare.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.provider.Settings
import android.text.TextUtils.SimpleStringSplitter
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.widget.Toast
import com.codewithkael.webrtcscreenshare.repository.MainRepository
import com.codewithkael.webrtcscreenshare.ui.CustomDialog
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WebrtcAccessibilityService @Inject constructor() : AccessibilityService() {

    private var intentFilter = IntentFilter()
    private var myBroadcastReceiver: WebrtcBroadcastReceiver? = null


//    @Inject
//    lateinit var mainRepository: MainRepository

    override fun onCreate() {
        super.onCreate()

        myBroadcastReceiver = WebrtcBroadcastReceiver(this)
        intentFilter.addAction("startBroadcastReceiver")
        registerReceiver(myBroadcastReceiver, intentFilter)
    }

    override fun onServiceConnected() {
        super.onServiceConnected()

        val info = AccessibilityServiceInfo()
        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_VISUAL
        info.notificationTimeout = 1000
        this.serviceInfo = info
        Log.e(TAG, "onServiceConnected : ")
    }


    override fun onInterrupt() {
        // 서비스 중단 시 호출되는 로직
        Log.e(TAG, "onInterrupt: something went wrong")
    }

    override fun onDestroy() {
        Log.d("MyService", "WebRtcCobrowseAccessibilityService 종료됨")
        unregisterReceiver(myBroadcastReceiver)
        //        unregisterReceiver(myBroadcastReceiver);
    }


    companion object {
        private const val TAG = "MyAccessibilityService"

        private var isGetAccessibilityServicePermission = false
        private fun isEnabled(context: Context): Boolean {
            try {
                val info = context.packageManager.getPackageInfo(
                    context.packageName,
                    PackageManager.GET_SERVICES
                )
                if (info.services == null) {
                    return false
                }
                val var2 = info.services
                val var3 = var2.size
                for (var4 in 0 until var3) {
                    val service = var2[var4]
                    if (WebrtcAccessibilityService::class.java.name == service.name) {
                        return "android.permission.BIND_ACCESSIBILITY_SERVICE" == service.permission
                    }
                }
            } catch (var6: PackageManager.NameNotFoundException) {
                Log.e("CobrowseIO", "Failed to read the app package info", var6)
            }
            return false
        }

        fun isRunning(context: Context): Boolean {
            val expectedComponentName =
                ComponentName(context, WebrtcAccessibilityService::class.java)
            val enabledServicesSetting =
                Settings.Secure.getString(context.contentResolver, "enabled_accessibility_services")
            return if (enabledServicesSetting == null) {
                false
            } else {
                val colonSplitter = SimpleStringSplitter(':')
                colonSplitter.setString(enabledServicesSetting)
                var enabledService: ComponentName?
                do {
                    if (!colonSplitter.hasNext()) {
                        return false
                    }
                    val componentNameString = colonSplitter.next()
                    enabledService = ComponentName.unflattenFromString(componentNameString)
                } while (enabledService == null || enabledService != expectedComponentName)
                true
            }
        }

        fun showSetup(mContext: Context, mActivity: Activity, customDialog: CustomDialog): Boolean {
            if (isEnabled(mContext)) {
                if (!isRunning(mContext)) {  // if accessibility service is not enabled yet
                    Log.e(
                        "CobrowseIO",
                        "Cobrowse accessibility service is enabled but not running. You need to enable it."
                    )

//                    val i = Intent(context, E9payCobrowseAccessibilitySetup::class.java)
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                    context.startActivity(i)

                    mActivity.runOnUiThread {
                        customDialog.show()
                    }

                    return false

                } else { // if accessibility service is already enabled
                    Log.i("CobrowseIO", "Cobrowse accessibility service is enabled and running")
                    mActivity.runOnUiThread {
                        Toast.makeText(mContext, "이미 허가 하셨습니다.", Toast.LENGTH_SHORT).show()
                    }
                    isGetAccessibilityServicePermission = true

                    return true
                }
            } else { // if accessibility service is already enabled
                Log.i("CobrowseIO", "Cobrowse accessibility service is enabled and running")
                isGetAccessibilityServicePermission = true

                return true
            }
        }
    }


    override fun onAccessibilityEvent(p0: AccessibilityEvent?) {
        Log.d("JaehongJaehong", p0?.eventType.toString())
    }
}
