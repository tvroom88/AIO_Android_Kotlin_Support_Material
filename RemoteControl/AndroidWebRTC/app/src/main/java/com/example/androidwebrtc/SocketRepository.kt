package com.example.androidwebrtc

import android.util.Log
import com.example.androidwebrtc.models.MessageModel
import com.example.androidwebrtc.utils.NewMessageInterface
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.java_websocket.client.WebSocketClient
import java.net.URI
import java.util.concurrent.TimeUnit

import org.java_websocket.handshake.ServerHandshake


/**
 * (1) 만약 안드로이드 에뮬레이터를 사용할 것이라면 "ws://10.0.2.2:3000"
 * (2) 개인 스마트폰으로 진행한다면 터미널에서 'ipconfig' 을 쳐서 개인의 ethernet ipv4을 찾기 ,
 *     제꺼는 오른쪽과 같습니다. "ws://172.16.35.222:3000"
 */
class SocketRepository(
    private val messageInterface: NewMessageInterface,
    private val userName: String
) : WebSocketListener() {

    private var webSocket: WebSocketClient? = null
    private val gson = Gson()
    private val uri = URI("ws://172.16.35.222:3000")

    fun initSocket() {

        webSocket = object : WebSocketClient(uri) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                sendMessageToSocket(
                    MessageModel(
                        "store_user", userName, null, null
                    )
                )
            }

            override fun onMessage(message: String?) {
                try {
                    messageInterface.onNewMessage(gson.fromJson(message, MessageModel::class.java))

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d("SocketRepository", "onClose: $reason")
            }

            override fun onError(ex: Exception?) {
                Log.d("SocketRepository", "onError: $ex")
            }

        }
        webSocket?.connect()

    }

    fun sendMessageToSocket(message: MessageModel) {
        try {
            Log.d("SocketRepository", "sendMessageToSocket: $message")
            webSocket?.send(Gson().toJson(message))
        } catch (e: Exception) {
            Log.d("SocketRepository", "sendMessageToSocket - error: $e")
        }
    }

}