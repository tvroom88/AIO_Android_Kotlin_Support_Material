package com.example.coroutineexample.coroutinebuilder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutineexample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class WithContextFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        Log.d("WithContextFragment", "onCreateView")

        CoroutineScope(Dispatchers.Default).launch {
            withContextExample()
        }
        withContextExample()
        return inflater.inflate(R.layout.fragment_with_context, container, false)
    }

    /**
     * withContext :
     *
     * a) 코루틴 내부의 Context(디스패쳐)를 변경할 수 있다.
     * b) 안드로이드의 경우 io나 default 쓰레드에서 동작하는 코루틴을 처리 완료된 이후
     * UI 쓰레드 작업이 필요할 경우 사용 가능
     */


    // withContext를 사용하면 result1, result2가 순차적으로 실행되고 결과를 언게 됩니다.
    private fun withContextExample() {
        Log.d("WithContextFragment", "--- Start ---")
        CoroutineScope(Dispatchers.Default).launch {
            val result1 = withContext(Dispatchers.IO) {
                // 첫 번째 작업: 네트워크 호출 등
                Log.d("WithContextFragment", "withContextExample - result1 inside")
                delay(500) // 1초 대기
                "Result 1"
            }
            Log.d("WithContextFragment", "withContextExample - result1 $result1")

            val result2 = withContext(Dispatchers.IO) {
                // 두 번째 작업: 다른 네트워크 호출 등
                Log.d("WithContextFragment", "withContextExample - result2 inside")

                delay(2000) // 1초 대기
                "Result 2"
            }
            Log.d("WithContextFragment", "withContextExample - result2 $result2")
            Log.d("WithContextFragment", "--- End ---")
        }


        // --- Start ---
        // withContextExample - result1 inside
        // withContextExample - result1 Result 1
        // withContextExample - result2 inside
        // withContextExample - result2 Result 2
        // --- End ---
    }

}