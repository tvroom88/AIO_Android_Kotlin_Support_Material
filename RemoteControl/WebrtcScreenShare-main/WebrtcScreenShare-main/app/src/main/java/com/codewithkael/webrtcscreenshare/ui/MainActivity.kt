package com.codewithkael.webrtcscreenshare.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.codewithkael.webrtcscreenshare.databinding.ActivityMainBinding
import com.codewithkael.webrtcscreenshare.repository.MainRepository
import com.codewithkael.webrtcscreenshare.service.WebrtcAccessibilityService
import com.codewithkael.webrtcscreenshare.service.WebrtcService
import com.codewithkael.webrtcscreenshare.service.WebrtcServiceRepository
import dagger.hilt.android.AndroidEntryPoint
import org.webrtc.DataChannel
import org.webrtc.MediaStream
import org.webrtc.RendererCommon.ScalingType
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), MainRepository.Listener {

    private var username: String? = null
    private lateinit var binding: ActivityMainBinding

    private var isGetAccessibilityServicePermission = false

    @Inject
    lateinit var webrtcServiceRepository: WebrtcServiceRepository

    @Inject
    lateinit var mainRepository: MainRepository

    private val capturePermissionRequestCode = 1

    private lateinit var customDialog: CustomDialog

    private lateinit var tempTarget: String
    private var isAllowedAccessibilityService: Boolean = false




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val observer: ViewTreeObserver = binding.surfaceView.viewTreeObserver
//        observer.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
//            override fun onGlobalLayout() {
//                val left: Int = binding.surfaceView.left
//                val top: Int = binding.surfaceView.top
//                val right: Int = binding.surfaceView.right
//                val bottom: Int = binding.surfaceView.bottom
//
//                Log.d("abcabcabc", "binding.surfaceView.left ViewTreeObserver" + left)
//                Log.d("abcabcabc", "binding.surfaceView.top ViewTreeObserver" + top)
//                binding.surfaceView.viewTreeObserver.removeGlobalOnLayoutListener(this)
//            }
//        })


        init()
    }

    override fun onResume() {
        super.onResume()
        if (customDialog.isShowing && WebrtcAccessibilityService.isRunning(this)) {
            customDialog.dismiss()
            webrtcServiceRepository.sendAccessibilityServiceStatusToServer(true, tempTarget)

            Log.d("Jaehong", "OnResumeOnResume")
        } else {

        }


    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        username = intent.getStringExtra("username")
        if (username.isNullOrEmpty()) {
            finish()
        }

        customDialog = CustomDialog(this)

        WebrtcService.surfaceView = binding.surfaceView
        WebrtcService.listener = this
        webrtcServiceRepository.startIntent(username!!)
        binding.requestBtn.setOnClickListener {
            startScreenCapture()
        }

        binding.testDataChannelButton.setOnClickListener {
            webrtcServiceRepository.sendDataChannelMessage()
        }

//        binding.surfaceView.setScalingType(ScalingType.SCALE_ASPECT_FILL)
        binding.surfaceView.setScalingType(ScalingType.SCALE_ASPECT_FILL)
        binding.surfaceView.requestLayout();


        binding.surfaceView.setOnTouchListener { v, event ->
            val action = event.action
            val curX = event.x // 눌린 곳의 X좌표
            val curY = event.y // 눌린 곳의 Y좌표

            when (action) {
                MotionEvent.ACTION_DOWN -> { // 처음 눌렸을 때
                    Log.d("setOnTouchListener", "손가락 눌림 : $curX, $curY")
                }

                MotionEvent.ACTION_MOVE -> { // 누르고 움직였을 때
                    Log.d("setOnTouchListener", "손가락 움직임 : $curX, $curY")
                }

                MotionEvent.ACTION_UP -> { // 누른 걸 뗐을 때
                    Log.d("setOnTouchListener", "손가락 뗌 : $curX, $curY")

                    //서버에서 좌표를 받아서 Accessibiltiy service에 포함도니 Broadcast Receiver에 보낸다.
                    if (isAllowedAccessibilityService) {
                        val intent = Intent("sendXandYCoordination")
//                    intent.putExtra("xRatio", latioX)
//                    intent.putExtra("yRatio", latioY)
                        sendBroadcast(intent)
                    }

                }
            }
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode != capturePermissionRequestCode) return
        WebrtcService.screenPermissionIntent = data
        webrtcServiceRepository.requestConnection(
            binding.targetEt.text.toString()
        )
    }

    private fun startScreenCapture() {
        val mediaProjectionManager = application.getSystemService(
            Context.MEDIA_PROJECTION_SERVICE
        ) as MediaProjectionManager

        startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(), capturePermissionRequestCode
        )
    }

    override fun onConnectionRequestReceived(target: String) {
        runOnUiThread {
            binding.apply {
                notificationTitle.text = "$target is requesting for connection"
                notificationLayout.isVisible = true
                notificationAcceptBtn.setOnClickListener {
                    webrtcServiceRepository.acceptCAll(target)
                    notificationLayout.isVisible = false

                    // adding remoteControl button only for callee to request remote control to caller
                    setRemoteControlBtn(target)
                }
                notificationDeclineBtn.setOnClickListener {
                    notificationLayout.isVisible = false
                }
            }
        }
    }

    // adding remoteControl button only for callee to request remote control to caller
    private fun setRemoteControlBtn(target: String) {
        Log.d("JaehongLee", "setRemoteControlBtn")

        binding.remoteControl.apply {
            isVisible = true
            setOnClickListener {
                webrtcServiceRepository.sendRequestRemoteControlPermission(target)
            }
        }
    }

    override fun onConnectionConnected() {
        runOnUiThread {
            binding.apply {
                requestLayout.isVisible = false
                disconnectBtn.isVisible = true
                disconnectBtn.setOnClickListener {
                    webrtcServiceRepository.endCallIntent()
                    restartUi()
                }
            }
        }
    }

    override fun onCallEndReceived() {
        runOnUiThread {
            restartUi()
        }
    }

    override fun onRemoteStreamAdded(stream: MediaStream) {
        runOnUiThread {
            binding.surfaceView.isVisible = true
            stream.videoTracks[0].addSink(binding.surfaceView)
        }
    }

    //Before starting remote control, user should enable to use accessibility service
    override fun openRequestRemoteControlPermissionView(target: String) {
        tempTarget = target

        // 이건 이미 true일 상황일때만임. 그렇기 때문에 accessibilityService에서 승낙하고 돌아올떄도
        if (WebrtcAccessibilityService.showSetup(this, this, customDialog)) {
            webrtcServiceRepository.sendAccessibilityServiceStatusToServer(true, target)
        } else {
            //OnResume
        }
    }


    override fun toastMessageForTest() {
        runOnUiThread {
            Toast.makeText(this, "Coming Here", Toast.LENGTH_SHORT).show()
            binding.textview123.isVisible = true
        }
    }


    override fun statusRemoteControlPermission(status: Boolean) {
        isAllowedAccessibilityService = status
    }


    private fun restartUi() {
        binding.apply {
            disconnectBtn.isVisible = false
            requestLayout.isVisible = true
            notificationLayout.isVisible = false
            surfaceView.isVisible = false
        }
    }


    override fun onDataChannelReceived() {
//        runOnUiThread {
//            views.apply {
//                requestLayout.isVisible = false
//                receivedDataLayout.isVisible = true
//                sendDataLayout.isVisible = true
//            }
//        }
    }

    override fun onDataReceivedFromChannel(it: DataChannel.Buffer) {
        Log.d("abcabc", "MainActivity")

//        runOnUiThread {
            val data: ByteBuffer = it.data
            val decodedString = StandardCharsets.UTF_8.decode(data).toString()

            Log.d("abcabc", decodedString)
//        }
    }




}