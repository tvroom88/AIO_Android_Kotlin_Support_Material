package com.example.androidwebsocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import org.json.JSONObject


class WebSocketHelper : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Log.d("WebSocketHelper", "WebSocket Connected")
        var openMessage = "{\"message\":\"hello\"}";
        webSocket.send(openMessage)
        webSocket.close(1000, null)
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
}