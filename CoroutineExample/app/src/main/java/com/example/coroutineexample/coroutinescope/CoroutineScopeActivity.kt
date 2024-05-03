package com.example.coroutineexample.coroutinescope

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.coroutineexample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 참고 링크 :
 * 1. https://velog.io/@devoks/Kotlin-Coroutines-Flow
 * 2. https://dongtrivia.com/entry/Android-Kotlin-CoroutineScope-%EC%97%90-%EB%8C%80%ED%95%B4-%EC%95%8C%EC%95%84%EB%B3%B4%EC%9E%90#:~:text=LifecycleScope,%ED%95%A0%20%EC%88%98%20%EC%9E%88%EB%8A%94%20Scope%20%EC%9E%85%EB%8B%88%EB%8B%A4.&text=%EC%9C%84%20lifecycleScope%20%EB%8A%94%20Activity%20%EC%95%85,%EB%82%B4%EB%B6%80%20%EC%9E%91%EC%97%85%EB%93%A4%EC%9D%B4%20%EC%B7%A8%EC%86%8C%EB%90%A9%EB%8B%88%EB%8B%A4
 * Coroutine Scope
 * 코루틴의 제어 범위, 실행범위를 지정할 수 있다.
 * 동일한 Scope안의 코루틴을 취소하는 경우 모든 자식 코루틴들도 취소된다. 하지만 다른 스코프를 지정하여 사용하는 코루틴은 취소되지 않는다.
 *
 * 1. CoroutineScope
 * a) 특정한 Scope 및 Dispatcher를 지정할 수 있는 기본 코루틴 스코프
 * b) 안드로이드 생명주기에 맞게 설계된 viewModelScope, LifecycleScope등이 제공된다.
 *
 * viewModelScope : viewModelScope의 생명주기는 ViewModel과 동일하여, ViewModel이 제거되면 모든 코루틴 작업이 자동 취소 됩니다.
 * LifeCycleScope : Lifecycle이 존재하는 Activity 나 Fragment 의 수명주기에 따라 자동으로 코루틴을 시작하고 취소할 수 있는 Scope 입니다.
 *
 */
class CoroutineScopeActivity : AppCompatActivity() {

    private lateinit var coroutineExampleViewModel: CoroutineExampleViewModel

    private lateinit var textViewName: TextView
    private lateinit var textViewAge: TextView
    private lateinit var testViewModelBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_scope)

        testViewModelBtn = findViewById(R.id.testViewModelBtn)
        textViewName = findViewById(R.id.textViewName)
        textViewAge = findViewById(R.id.textViewAge)

        coroutineExampleViewModel = ViewModelProvider(this)[CoroutineExampleViewModel::class.java]


        // 1. CoroutineScope
        val scopeMain = CoroutineScope(Dispatchers.Main)
        scopeMain.launch {
            // 메인 쓰레드 작업
        }

        CoroutineScope(Dispatchers.IO).launch {
            // 백그라운드 작업
        }


        // 2. viewModelScope
        coroutineExampleViewModel.userDataLiveData.observe(this, Observer { userData ->
            // UI 업데이트
            textViewName.text = userData.name
            textViewAge.text = userData.age.toString()
        })

        testViewModelBtn.setOnClickListener {
            coroutineExampleViewModel.fetchData()
        }

        // 3. lifeCycleScope
        lifecycleScope.launch {
            delay(1000)
        }
    }

    //동일한 Scope안의 코루틴을 취소하는 경우 모든 자식 코루틴들도 취소된다. 하지만 다른 스코프를 지정하여 사용하는 코루틴은 취소되지 않는다.
    private fun test(){

    }


}