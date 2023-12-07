package com.example.androidwebsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        client = OkHttpClient()

        val request: Request = Request.Builder()
            .url(url)
            .build()
        val listener = WebSocketHelper()

        client.newWebSocket(request, listener)
        client.dispatcher.executorService.shutdown()
    }


    companion object {
        private const val url = "ws://192.168.200.17:3000"
    }
}