package com.example.coroutineexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

/**
 * 코루틴은 시작된 스레드를 중단하지 않으면서 비동기적으로 실행되는 코드입니다.
 * 기존의 복잡한 AsyncTask 또는 다수 스레드 관리를 직접 해주지 않아도 되며, 기존 다
 */

class MainActivity : AppCompatActivity() {

    private lateinit var testBtn1: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testBtn1 = findViewById(R.id.testBtn1)

        testBtn1.setOnClickListener {
            Log.d("testtest", "testtest")
        }


        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val name = getFirstName()
            val lastName = getLastName()
            println("Hello, $name $lastName")
        }
    }


    suspend fun getFirstName() : String{
        delay(1000)
        return "Hong"
    }

    suspend fun getLastName() : String{
        delay(1000)
        return "Coding"
    }
}