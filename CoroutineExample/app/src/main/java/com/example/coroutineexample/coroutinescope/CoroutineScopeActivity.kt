package com.example.coroutineexample.coroutinescope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.coroutineexample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 참고 링크 :
 * 1. https://velog.io/@devoks/Kotlin-Coroutines-Flow
 * 2.
 * Coroutine Scope
 * 코루틴의 제어 범위, 실행범위를 지정할 수 있다.
 * 동일한 Scope안의 코루틴을 취소하는 경우 모든 자식 코루틴들도 취소된다. 하지만 다른 스코프를 지정하여 사용하는 코루틴은 취소되지 않는다.
 *
 * 1. CoroutineScope
 * - xmr
 */
class CoroutineScopeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_scope)

//        CoroutineScope(Dispatchers.Default).launch {
//            withContextExample()
//        }


    }




}