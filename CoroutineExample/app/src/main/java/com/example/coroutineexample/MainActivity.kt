package com.example.coroutineexample

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

/**
 * 코루틴은 시작된 스레드를 중단하지 않으면서 비동기적으로 실행되는 코드입니다.
 * 기존의 복잡한 AsyncTask 또는 다수 스레드 관리를 직접 해주지 않아도 되며, 기존 다
 */

class MainActivity : AppCompatActivity() {


    private lateinit var testBtn1: Button
    private var startTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testBtn1 = findViewById(R.id.testBtn1)

        testBtn1.setOnClickListener {
            Log.d("testtest", "testtest")
        }


        //결과값은 Hello, Hong Coding => 총 2초가 소요된다.
        test1()


        test2()


        test3()

    }

    private fun test1() {
        startTime = System.currentTimeMillis()

        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val name = getFirstName()
            val lastName = getLastName()
            println("test - test1 : Hello, $name $lastName")

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime
            val elapsedTimeInSeconds = elapsedTime / 1000.0 // 밀리초를 초로 변환
            println("test - test1 : 함수 실행에 걸린 시간: " + elapsedTimeInSeconds + "초");
        }
    }

    private fun test2() {
        startTime = System.currentTimeMillis()

        runBlocking {
            val name = getFirstName()
            val lastName = getLastName()
            println("test - test2 : Hello, $name $lastName")

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime
            val elapsedTimeInSeconds = elapsedTime / 1000.0 // 밀리초를 초로 변환
            println("test - test2 : 함수 실행에 걸린 시간: " + elapsedTimeInSeconds + "초");
        }

    }

    private fun test3() {
        startTime = System.currentTimeMillis()

        runBlocking {
            val name = async { getFirstName() }
            val lastName = async { getLastName() }
            println("test - test3 : Hello, ${name.await()} ${lastName.await()}")

            val endTime = System.currentTimeMillis()
            val elapsedTime = endTime - startTime
            val elapsedTimeInSeconds = elapsedTime / 1000.0 // 밀리초를 초로 변환
            println("test - test3 : 함수 실행에 걸린 시간: " + elapsedTimeInSeconds + "초");
        }

    }

    suspend fun test4() = coroutineScope {
        val job = Job()
        CoroutineScope(Dispatchers.Default + job).launch {
            launch {
                println("Coroutine1 start")
                delay(1000)
                println("Coroutine1 end")
            }
            launch {
                println("Coroutine2 start")
                delay(1000)
                println("Coroutine2 end")
            }
            delay(1000)
            job.cancel()
            delay(2000)
            println("Finish")
        }
    }

    suspend fun getFirstName(): String {
        delay(1000)
        return "Hong"
    }

    suspend fun getLastName(): String {
        delay(1000)
        return "Coding"
    }
}