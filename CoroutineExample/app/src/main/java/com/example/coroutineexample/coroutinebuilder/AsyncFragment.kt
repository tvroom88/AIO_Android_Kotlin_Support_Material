package com.example.coroutineexample.coroutinebuilder

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.coroutineexample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

/**
 * 참고 링크:
 * 1. https://velog.io/@devoks/Kotlin-Coroutines-Flow
 * 2.
 */
class AsyncFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        CoroutineScope(Dispatchers.Main).launch {
            asyncFunction()
        }
        return inflater.inflate(R.layout.fragment_async, container, false)
    }


    /**
     * async :
     * a) Deferred 객체를 반환하고, 그와 함께 결과를 받을 수 있다.
     * b) deferred.await() 메소드를 사용하여 결과를 받는다.
     *
     * async와 await을 사용해서 결과를 받아보고 await 아래 코드를 진행시킬수 있다.
     */
    private suspend fun asyncFunction() {
        Log.d("asyncFunction", "1. start asyncFunction")

        val deferred:Deferred<String> = CoroutineScope(Dispatchers.Default).async {
            Log.d("asyncFunction", "2. start deferred")
            sleep(2000)
            Log.d("asyncFunction", "3. end deferred")
            "abcd"
        }

        Log.d("asyncFunction", "4")
        val num = deferred.await()
        Log.d("asyncFunction", "5. num : $num")
        Log.d("asyncFunction", "6. end asyncFunction")

        // 1. start asyncFunction
        // 4
        // 2. start deferred
        // 3. end deferred
        // 5. num : 31
        // 6. end asyncFunction
    }

}