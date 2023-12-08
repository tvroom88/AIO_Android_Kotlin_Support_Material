package com.example.myapplication

import android.util.Log
import com.example.myapplication.interfaces.NewMsgInterface
import com.example.myapplication.models.DataModel

import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class SocketRepository(private val msgInterface: NewMsgInterface) {

    private lateinit var webSocket: WebSocketClient
    private val gson = Gson()

    // (1) 만약 안드로이드 에뮬레이터를 사용할 것이라면 "ws://10.0.2.2:3000"
    // (2) 개인 스마트폰으로 진행한다면 터미널에서 'ipconfig' 을 쳐서 개인의 ethernet ipv4을 찾기 , 제꺼는 오른쪽과 같습니다.: "ws://192.168.200.17:3000"


    private val url = URI("ws://192.168.153.165")

    companion object {
        private const val TAG: String = "SocketRepository"
    }

    fun initSocket(username: String) {

        val openMsg = DataModel("store_user", username, null, null)
        Log.d(TAG, "initSocket - start socket")

        webSocket = object : WebSocketClient(url) {
            override fun onOpen(handshakedata: ServerHandshake?) {
                try {
                    webSocket.send(Gson().toJson(openMsg))
                    Log.d(TAG, "sendMessageToServer - Msg : $openMsg")
                } catch (e: Exception) {
                    Log.d(TAG, "sendMessageToServer - error : $e")
                }
            }

            override fun onMessage(message: String) {
                try {
                    Log.d(TAG, "onMessage - getNewMsg : $message")
                    msgInterface.onNewMsg(gson.fromJson(message, DataModel::class.java))
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose: $reason")
            }

            override fun onError(ex: Exception?) {
                Log.d(TAG, "onError: $ex")
            }

        }
        webSocket.connect()
    }

    fun sendMessageToSocket(message: DataModel) {
        try {
            Log.d(TAG, "sendMessageToSocket: $message")
            webSocket?.send(Gson().toJson(message))
        } catch (e: Exception) {
            Log.d(TAG, "sendMessageToSocket: $e")
        }
    }
}