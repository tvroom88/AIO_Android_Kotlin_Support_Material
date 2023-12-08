package com.example.androidwebsocket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var webSocketHelper: WebSocketHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        val button = findViewById<Button>(R.id.button)

        webSocketHelper = WebSocketHelper()
        webSocketHelper.initWebSocket()

        button.setOnClickListener {
            var editStr: String = if (editText.text.toString() == "") "null" else editText.text.toString()
            val str = webSocketHelper.convertStrToJsonStr("{message:$editStr}")
            webSocketHelper.sendMessageToServer(str)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketHelper.closeWebSocket()
    }
}