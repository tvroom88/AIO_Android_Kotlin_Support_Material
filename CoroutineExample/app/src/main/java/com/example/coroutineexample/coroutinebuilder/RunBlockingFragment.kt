package com.example.coroutineexample.coroutinebuilder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutineexample.R
import kotlinx.coroutines.runBlocking

class RunBlockingFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        runBlockingFunction()

        return inflater.inflate(R.layout.fragment_run_blocking, container, false)
    }

    /**
     * runBlocking
     * a) 새로운 코루틴을 실행하고 완료 될 때까지 스레드를 차단한다.
     * Android UI 스레드에서 사용하는 것을 되도록 피해야 한다.
     */

    private fun runBlockingFunction() {
        Log.d("runBlockingFunction", "1. start runBlockingFunction")

        val result = runBlocking {
            Log.d("runBlockingFunction", "2. start deferred")
            Thread.sleep(2000)
            Log.d("runBlockingFunction", "3. end deferred")
        }

        Log.d("runBlockingFunction", "4")
        Log.d("runBlockingFunction", "5. result : $result")
        Log.d("runBlockingFunction", "6. end runBlockingFunction")

        // 1. start runBlockingFunction
        // 2. start deferred
        // 3. end deferred
        // 4
        // 5. result : 37
        // 6. end runBlockingFunction
    }
}