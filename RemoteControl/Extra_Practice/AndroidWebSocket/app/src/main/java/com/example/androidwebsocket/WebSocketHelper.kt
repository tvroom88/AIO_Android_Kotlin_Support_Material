package com.example.androidwebsocket

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class WebSocketHelper : WebSocketListener() {

    private lateinit var webSocket: WebSocket
    private lateinit var client: OkHttpClient
    private lateinit var request: Request

    fun initWebSocket() {
        client = OkHttpClient.Builder()
            .pingInterval(30, TimeUnit.SECONDS)
            .build()
        request = Request.Builder()
            .url(url)
            .build()
        webSocket = client.newWebSocket(request, this)
    }


    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocketHelper", "WebSocket Connected")
        webSocket.send(convertStrToJsonStr("{message:hello}"))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        Log.d("WebSocketHelper", "Receiving Message - text : $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Log.d("WebSocketHelper", "Receiving Bytes : $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Log.d("WebSocketHelper", "Closing coded : $code, Closing Reason $reason")
        webSocket.close(1000, null)
        webSocket.cancel()
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d("WebSocketHelper", "error message : ${t.message}, and error Response : $response")
    }

    fun convertStrToJsonStr(str: String): String {
        val jsonObject = JSONObject(str)
        return jsonObject.toString()
    }

    fun sendMessageToServer(text: String) {
        webSocket.send(text)
    }

    fun closeWebSocket() {
        webSocket.close(1000, "time to close WebSocket")
        client.dispatcher.executorService.shutdown()
    }

    companion object {
        private const val url = "ws://192.168.200.17:3000"
    }
}