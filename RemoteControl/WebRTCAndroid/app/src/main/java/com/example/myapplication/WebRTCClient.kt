package com.example.myapplication

import android.app.Application
import com.example.myapplication.models.DataModel
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraVideoCapturer
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.MediaConstraints
import org.webrtc.PeerConnection
import org.webrtc.PeerConnection.IceServer
import org.webrtc.PeerConnectionFactory
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

class WebRTCClient(
    private val application: Application,
    private val username: String,
    private val socketRepository: SocketRepository,
    private val observer: PeerConnection.Observer
) {

    /**
     * EglBase : EGL 컨텍스트를 효과적으로 생성하고 관리
     * EGL(Embedded Graphics Library) : Android에서는 그래픽 컨텍스트를 생성하고 제어하는 데에 EGL이 사용됩니다.
     * PeerConnectionFactory : 실시간으로 오디오, 비디오, 데이터를 교환하고 미디어 스트림을 생성하고 관리
     * PeerConnection :  두 피어 간의 연결을 나타내는 클래스입니다
     * VideoSource : 비디오 소스, 카메라에서 비디오를 캡처하고 전송하기 위해 사용
     * AudioSource : 오디오 소스, 오디오를 캡처하고 전송하기 위해 사용
     * CameraVideoCapturer :카메라 비디오 캡처를 수행
     * AudioTrack :  로컬 사용자의 마이크에서 받은 오디오를 표현
     * VideoTrack : 로컬 사용자의 카메라에서 받은 비디오를 표현
     */
    private val eglContext = EglBase.create()
    private var peerConnectionFactory: PeerConnectionFactory
    private var peerConnection: PeerConnection? = null
    private var localVideoSource: VideoSource
    private var localAudioSource: AudioSource
    private lateinit var videoCapturer: CameraVideoCapturer
    private lateinit var localAudioTrack: AudioTrack
    private lateinit var localVideoTrack: VideoTrack


    private val iceServer = listOf(
        IceServer.builder("stun:iphone-stun.strato-iphone.de:3478").createIceServer(),
        IceServer.builder("stun:openrelay.metered.ca:80").createIceServer(),
        IceServer.builder("turn:openrelay.metered.ca:80")
            .setUsername("openrelayproject")
            .setPassword("openrelayproject")
            .createIceServer(),
        IceServer.builder("turn:openrelay.metered.ca:443")
            .setUsername("openrelayproject")
            .setPassword("openrelayproject")
            .createIceServer(),
        IceServer.builder("turn:openrelay.metered.ca:443?transport=tcp")
            .setUsername("openrelayproject")
            .setPassword("openrelayproject")
            .createIceServer()
    )

    init {
        initPeerConnectionFactory(application)
        peerConnectionFactory = createPeerConnectionFactory()
        peerConnection = createPeerConnection(observer)
        localVideoSource = peerConnectionFactory.createVideoSource(false)
        localAudioSource = peerConnectionFactory.createAudioSource(MediaConstraints())
    }

    private fun initPeerConnectionFactory(application: Application) {
        val peerConnectionOption = PeerConnectionFactory.InitializationOptions.builder(application)
            .setEnableInternalTracer(true)
            .setFieldTrials("WebRTC-H264HighProfile/Enabled/")
            .createInitializationOptions()

        PeerConnectionFactory.initialize(peerConnectionOption)
    }

    private fun createPeerConnectionFactory(): PeerConnectionFactory {
        return PeerConnectionFactory.builder()
            .setVideoEncoderFactory(
                DefaultVideoEncoderFactory(
                    eglContext.eglBaseContext,
                    true,
                    true
                )
            )
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglContext.eglBaseContext))
            .setOptions(PeerConnectionFactory.Options().apply {
                disableEncryption = true
                disableNetworkMonitor = true
            }).createPeerConnectionFactory()
    }

    private fun createPeerConnection(observer: PeerConnection.Observer): PeerConnection? {
        return peerConnectionFactory.createPeerConnection(iceServer, observer)
    }

    fun initializeSurfaceView(surface: SurfaceViewRenderer) {
        surface.run {
            setEnableHardwareScaler(true)
            setMirror(true)
            init(eglContext.eglBaseContext, null)
        }
    }

    fun startLocalVideo(surface: SurfaceViewRenderer) {
        val surfaceTextureHelper = SurfaceTextureHelper.create(Thread.currentThread().name, eglContext.eglBaseContext)
        videoCapturer = Camera2Enumerator(application).run {
            deviceNames.find { isFrontFacing(it) }?.let { createCapturer(it, null) } ?: throw IllegalStateException()
        }

        videoCapturer.initialize(surfaceTextureHelper, surface.context, localVideoSource.capturerObserver)

        videoCapturer.startCapture(320, 240, 30)

        localVideoTrack = peerConnectionFactory.createVideoTrack("local_track", localVideoSource)
        localVideoTrack.addSink(surface)

        localAudioTrack =
            peerConnectionFactory.createAudioTrack("local_track_audio", localAudioSource)

        val localStream = peerConnectionFactory.createLocalMediaStream("local_stream")
        localStream.addTrack(localAudioTrack)
        localStream.addTrack(localVideoTrack)

        peerConnection?.addStream(localStream)
    }

    fun call(target: String) {
        val mediaConstraints = MediaConstraints()
        mediaConstraints.mandatory.add(MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"))
        peerConnection?.createOffer(customSdpObserver, mediaConstraints)
    }


    private val customSdpObserver = object : SdpObserver {
        override fun onCreateSuccess(desc: SessionDescription?) {
            peerConnection?.setLocalDescription(object : SdpObserver {
                override fun onCreateSuccess(p0: SessionDescription?) {

                }

                override fun onSetSuccess() {
                    val offer = hashMapOf(
                        "sdp" to desc?.description,
                        "type" to desc?.type
                    )

                    socketRepository.sendMessageToSocket(
                        DataModel(
                            "create_offer", username, target, offer
                        )
                    )
                }

                override fun onCreateFailure(p0: String?) {
                }

                override fun onSetFailure(p0: String?) {
                }

            }, desc)

        }

        override fun onSetSuccess() {
        }

        override fun onCreateFailure(p0: String?) {
        }

        override fun onSetFailure(p0: String?) {
        }
    }

}