package com.example.coroutineexample

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutineexample.coroutinebuilder.CoroutineBuilderActivity
import com.example.coroutineexample.coroutinescope.CoroutineScopeActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.Thread.sleep


/**
 * 1) https://velog.io/@devoks/Kotlin-Coroutines-Flow
 * 2) https://wooooooak.github.io/kotlin/2019/06/18/coroutineStudy/
 * 3) CoroutineBuilder - Job 내용 : https://velog.io/@jaewonkim1468/Coroutine%EC%9D%98-Job
 */

/**
 * CoroutineBuilder (코루틴 생성) 차례 :
 *
 * 1. Coroutine launch
 *    a) launch (non-blocking)
 *    b) async (non-blocking)
 *    c) runBlocking (blocking)
 *
 */
class MainActivity : AppCompatActivity() {

    private lateinit var testBtn1: Button
    private lateinit var testBtn2: Button

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testBtn1 = findViewById(R.id.testBtn1)
        testBtn1.setOnClickListener {
            val intent = Intent(this, CoroutineBuilderActivity::class.java)
            startActivity(intent)
        }

        testBtn2 = findViewById(R.id.testBtn2)
        testBtn2.setOnClickListener {
            val intent = Intent(this, CoroutineScopeActivity::class.java)
            startActivity(intent)
        }

//        GlobalScope.launch {
//            checkJob()
//        }
//        buildByUsingAsync()
    }


//    /**
//     * 2. async (non-blocking)
//     *
//     * a) 결과가 예상되는 코루틴 시작에 사용(결과 반환)
//     * b) 전역으로 예외 처리 가능
//     * c) 결과, 예외 반환 가능한 Deferred 반환
//     */
//    private fun buildByUsingAsync() {
//        Log.d("MainActivity-a", "buildByUsingAsync - buildByUsingLaunch 시작")
//
//        // async를 사용하면 Defferred<타입>이 되는 것 같다.
//        var stock1: Deferred<Int>
//        var stock2: Deferred<Int>
//        var stock3: Deferred<Int>
//
//        coroutineScope = CoroutineScope(Dispatchers.Main)
//        coroutineScope.async {
//
//            // a) async만 사용
//            stock1 = async { getStock1() }
//
//            // b) async에 thread 지정
//            stock2 = async(Dispatchers.IO) { getStock2() }
//
//            // c) 마지막게 return 되는 것 같다.
//            stock3 = async(Dispatchers.IO) {
//                delay(1000)
//                Log.i("MyTag", "stock 1 returned")
//                1000
//            }
//
//            val total = stock1.await() + stock2.await() + stock3.await()
//
//            Log.i("MainActivity-a", "Total is $total")
//        }
//        Log.d("MainActivity-a", "buildByUsingAsync - buildByUsingLaunch 끝")
//    }
//
//    private suspend fun getStock1(): Int {
//        delay(1000)
//        Log.i("MyTag", "stock 1 returned")
//        return 55000
//    }
//
//    private suspend fun getStock2(): Int {
//        delay(1000)
//        Log.i("MyTag", "stock 2 returned")
//        return 55000
//    }
}
