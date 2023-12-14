package com.example.androidwebrtc

import android.content.Intent
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.androidwebrtc.databinding.ActivityCallBinding
import com.example.androidwebrtc.models.IceCandidateModel
import com.example.androidwebrtc.models.MessageModel
import com.example.androidwebrtc.utils.CaptureMode
import com.example.androidwebrtc.utils.MessageInterface
import com.example.androidwebrtc.utils.PeerConnectionObserver
import com.example.androidwebrtc.utils.RTCAudioManager
import com.google.gson.Gson
import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription
import java.io.Serializable


class CallActivity : AppCompatActivity(), MessageInterface {

    lateinit var binding: ActivityCallBinding
    private var userName: String? = null
    private var captureMode: CaptureMode? = null

    private var socketRepository: SocketRepository? = null
    private var rtcClient: RTCClient? = null
    private val TAG = "WebRTC - CallActivity"
    private var target: String = ""
    private val gson = Gson()
    private var isMute = false
    private var isCameraPause = false
    private val rtcAudioManager by lazy { RTCAudioManager.create(this) }
    private var isSpeakerMode = true

    // ScreenSharing일 경우
    private var mediaProjectionManager: MediaProjectionManager? = null

    val PERMISSION_CODE = 1234
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var mediaProjectionPermissionResultData: Intent? = null

    private lateinit var foregroundServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userName = intent.getStringExtra("username") // 유저 이름
        captureMode = intent.intentSerializable(
            "captureMode",
            CaptureMode::class.java
        ) // 비디오를 보여줄지 스크린을 보여줄지 결정

        Log.d(TAG, "userName : $userName")
        Log.d(TAG, "captureMode : $captureMode")


        userName?.let {
            socketRepository = SocketRepository(this, it, this, this)
        }
        socketRepository?.initSocket()

        rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

        initRTCCLient()
        initButtons()


//        init();
        foregroundServiceIntent = Intent(this, WebRTCForegroundService::class.java)


        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                mediaProjectionPermissionResultData = it.data
                rtcClient?.startScreenShare(
                    mediaProjectionPermissionResultData!!,
                    this,
                    binding.localView
                )
                rtcClient?.call(binding.targetUserNameEt.text.toString())
                binding.button.visibility = View.VISIBLE
            }
        }


    }


//    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == PERMISSION_CODE) {
//            if (resultCode == RESULT_OK) {
//                // MediaProjection 객체 생성
////                MediaProjection mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data);
//                mediaProjectionPermissionResultData = data
//                rtcClient!!.startLocalVideo(
//                    mediaProjectionPermissionResultData,
//                    this
//                )
//                rtcClient!!.call(targetUserNameEt.text.toString())
//            }
//        }
//    }

    override fun onStop() {
        super.onStop()
        releaseSurfaceView()
        rtcClient?.endCall()
    }


    private fun initRTCCLient() {
        rtcClient = RTCClient(
            application,
            userName!!,
            socketRepository!!,
            object : PeerConnectionObserver() {
                override fun onIceCandidate(p0: IceCandidate?) {
                    super.onIceCandidate(p0)
                    rtcClient?.addIceCandidate(p0)
                    val candidate = hashMapOf(
                        "sdpMid" to p0?.sdpMid,
                        "sdpMLineIndex" to p0?.sdpMLineIndex,
                        "sdpCandidate" to p0?.sdp
                    )

                    socketRepository?.sendMessageToSocket(
                        MessageModel("ice_candidate", userName, target, candidate)
                    )

                }

                override fun onAddStream(p0: MediaStream?) {
                    super.onAddStream(p0)
                    p0?.videoTracks?.get(0)?.addSink(binding.remoteView)
                    Log.d(TAG, "onAddStream: $p0")
                }
            })
    }

    private fun initButtons() {
        binding.apply {
            callBtn.setOnClickListener {
                socketRepository?.sendMessageToSocket(
                    MessageModel(
                        "start_call", userName, targetUserNameEt.text.toString(), null
                    )
                )
                target = targetUserNameEt.text.toString()
            }

            switchCameraButton.setOnClickListener {
                rtcClient?.switchCamera()
            }

            micButton.setOnClickListener {
                if (isMute) {
                    isMute = false
                    micButton.setImageResource(R.drawable.ic_baseline_mic_off_24)
                } else {
                    isMute = true
                    micButton.setImageResource(R.drawable.ic_baseline_mic_24)
                }
                rtcClient?.toggleAudio(isMute)
            }

            videoButton.setOnClickListener {
                if (isCameraPause) {
                    isCameraPause = false
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_off_24)
                } else {
                    isCameraPause = true
                    videoButton.setImageResource(R.drawable.ic_baseline_videocam_24)
                }
                rtcClient?.toggleCamera(isCameraPause)
            }

            audioOutputButton.setOnClickListener {
                if (isSpeakerMode) {
                    isSpeakerMode = false
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_hearing_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.EARPIECE)
                } else {
                    isSpeakerMode = true
                    audioOutputButton.setImageResource(R.drawable.ic_baseline_speaker_up_24)
                    rtcAudioManager.setDefaultAudioDevice(RTCAudioManager.AudioDevice.SPEAKER_PHONE)

                }

            }
            endCallButton.setOnClickListener {
                setCallLayoutGone()
                setWhoToCallLayoutVisible()
                setIncomingCallLayoutGone()
//                rtcClient?.endCall()

                releaseSurfaceView()
            }
        }

    }

    private fun startService() {
        Thread {
            foregroundServiceIntent = Intent(this, WebRTCForegroundService::class.java)
            foregroundServiceIntent.action = "START_SERVICE"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(foregroundServiceIntent)
            } else {
                startService(foregroundServiceIntent)
            }
        }.start()
    }


    override fun onNewMessage(message: MessageModel) {
        Log.d(TAG, "onNewMessage: $message")
        when (message.type) {

            "store_user_response" -> {
                if (message.msg == "success") {
                    runOnUiThread {
                        Toast.makeText(this, "store user successfully", Toast.LENGTH_LONG).show()
                    }

                    Log.d(TAG, "store_user_response")

                } else {
                    val intent = Intent()
                    intent.putExtra(
                        "message",
                        "user name is already exist"
                    )
                    setResult(RESULT_OK, intent)
                    finish()
                }
            }


            "call_response" -> {
                if (message.data == "user is not online") {
                    //user is not reachable
                    runOnUiThread {
                        Toast.makeText(this, "user is not reachable", Toast.LENGTH_LONG).show()
                    }
                } else {
                    //we are ready for call, we started a call
                    runOnUiThread {
                        setWhoToCallLayoutGone()
                        setCallLayoutVisible()

                        if (captureMode == CaptureMode.VIDEO_CAPTURE) {
                            binding.apply {
                                rtcClient?.initializeSurfaceView(localView)
                                rtcClient?.initializeSurfaceView(remoteView)
                                rtcClient?.startLocalVideo(localView)
                                rtcClient?.call(targetUserNameEt.text.toString())
                            }
                        } else {

                            //추가된 부분
                            rtcClient?.initializeSurfaceView(binding.localView)
                            rtcClient?.call(binding.targetUserNameEt.text.toString())

                            startService()
                            mediaProjectionManager =
                                getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
                            launcher.launch(mediaProjectionManager!!.createScreenCaptureIntent())

                        }

                        Log.d(TAG, "call_response")

                    }

                }
            }

            //전화를 건 사람
            "answer_received" -> {

                val session = SessionDescription(
                    SessionDescription.Type.ANSWER,
                    message.data.toString()
                )
                rtcClient?.onRemoteSessionReceived(session)
                runOnUiThread {
                    binding.remoteViewLoading.visibility = View.GONE
                }

                Log.d(TAG, "answer_received")
            }

            //전화를 받은 사람
            "offer_received" -> {
                runOnUiThread {
                    setIncomingCallLayoutVisible()
                    binding.incomingNameTV.text = "${message.name.toString()} is calling you"
                    binding.acceptButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                        setCallLayoutVisible()
                        setWhoToCallLayoutGone()

                        binding.apply {
                            rtcClient?.initializeSurfaceView(localView)
                            rtcClient?.initializeSurfaceView(remoteView)
//                            rtcClient?.startLocalVideo(localView)
                        }
                        val session = SessionDescription(
                            SessionDescription.Type.OFFER,
                            message.data.toString()
                        )
                        rtcClient?.onRemoteSessionReceived(session)
                        rtcClient?.answer(message.name!!)
                        target = message.name!!
                        binding.remoteViewLoading.visibility = View.GONE

                    }
                    binding.rejectButton.setOnClickListener {
                        setIncomingCallLayoutGone()
                    }


                    Log.d(TAG, "offer_received")
                }

            }


            "ice_candidate" -> {
                try {
                    val receivingCandidate = gson.fromJson(
                        gson.toJson(message.data),
                        IceCandidateModel::class.java
                    )
                    rtcClient?.addIceCandidate(
                        IceCandidate(
                            receivingCandidate.sdpMid,
                            Math.toIntExact(receivingCandidate.sdpMLineIndex.toLong()),
                            receivingCandidate.sdpCandidate
                        )
                    )

                    Log.d(TAG, "ice_candidate")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun setIncomingCallLayoutGone() {
        binding.incomingCallLayout.visibility = View.GONE
    }

    private fun setIncomingCallLayoutVisible() {
        binding.incomingCallLayout.visibility = View.VISIBLE
    }

    private fun setCallLayoutGone() {
        binding.callLayout.visibility = View.GONE
    }

    private fun setCallLayoutVisible() {
        binding.callLayout.visibility = View.VISIBLE
    }

    private fun setWhoToCallLayoutGone() {
        binding.whoToCallLayout.visibility = View.GONE
    }

    private fun setWhoToCallLayoutVisible() {
        binding.whoToCallLayout.visibility = View.VISIBLE
    }

    //ENUM을 받을때 바뀐 부분
    private fun <T : Serializable> Intent.intentSerializable(key: String, clazz: Class<T>): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            this.getSerializableExtra(key, clazz)
        } else {
            this.getSerializableExtra(key) as T?
        }
    }

    private fun releaseSurfaceView() {
        binding.apply {
            rtcClient?.releaseSurfaceView(localView)
            rtcClient?.releaseSurfaceView(remoteView)
            rtcClient?.stopLocalVideo()
        }
    }


}