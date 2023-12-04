package com.example.myapplication

import android.nfc.Tag
import android.util.Log
import com.example.myapplication.interfaces.NewMsgInterface
import com.example.myapplication.models.Model

import com.google.gson.Gson
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class SocketRepository(private val msgInterface: NewMsgInterface) {

    private lateinit var webSocket: WebSocketClient
    private lateinit var userName: String
    private val gson = Gson()
    private val url = URI("ws://10.0.2.2:3000")

    companion object {
        private const val TAG: String = "SocketRepository"
    }

    fun initSocket(username: String) {
        userName = username

        //if you are using android emulator your local websocket address is going to be "ws://10.0.2.2:3000"
        //if you are using your phone as emulator your local address, use cmd and then write ipconfig
        // and get your ethernet ipv4 , mine is : "ws://192.168.1.3:3000"
        //but if your websocket is deployed you add your websocket address here

        webSocket = object : WebSocketClient(url) {
            override fun onOpen(handshakedata: ServerHandshake?) {
//                sendMessageToSocket(
//                    MessageModel(
//                        "store_user", username, null, null
//                    )
//                )
            }

            override fun onMessage(message: String) {
                try {
                    msgInterface.onNewMsg(gson.fromJson(message, Model::class.java))
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

//    fun sendMessageToSocket(message: MessageModel) {
//        try {
//            Log.d(TAG, "sendMessageToSocket: $message")
//            webSocket?.send(Gson().toJson(message))
//        } catch (e: Exception) {
//            Log.d(TAG, "sendMessageToSocket: $e")
//        }
//    }
}