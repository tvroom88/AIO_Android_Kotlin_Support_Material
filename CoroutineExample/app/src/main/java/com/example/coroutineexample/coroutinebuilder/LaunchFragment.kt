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
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LaunchFragment : Fragment() {

    private lateinit var coroutineScope: CoroutineScope

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        coroutineScope = CoroutineScope(Dispatchers.Main)

        /**
         * CoroutineBuilder (코루틴 생성) 차례 :
         *
         * 1. Coroutine launch
         * 1-1. Job
         *
         */

        buildByUsingLaunch() // 1. Coroutine launch
//        jobStart() //  1-1. Job

        CoroutineScope(Dispatchers.Main).launch {
            jobJoin()
        }

        return inflater.inflate(R.layout.fragment_launch, container, false)
    }


    // --------------- 1. Coroutine Builder ---------------
    /**
     *  1. launch (non-blocking)
     *
     * a) 결과를 반환하지 않는 코루틴 시작에 사용(결과 반환X)
     * b) 자체/ 자식 코루틴 실행을 취소할 수 있는 Job 반환
     */
    private fun buildByUsingLaunch() {
        Log.d("MainActivity-a", "buildByUsingLaunch - buildByUsingLaunch 시작")
        var testTxt1 = ""
        var testTxt2 = ""
        coroutineScope = CoroutineScope(Dispatchers.Main)
        val job = coroutineScope.launch {
            Log.d("MainActivity-a", "buildByUsingLaunch - coroutineScope 영역 시작")
            delay(3000)
            Log.d("MainActivity-a", "buildByUsingLaunch - coroutineScope 영역 끝")
        }
        Log.d("MainActivity-a", "buildByUsingLaunch - buildByUsingLaunch 끝")

        // 1 - buildByUsingLaunch - buildByUsingLaunch 시작
        // 2 - buildByUsingLaunch - buildByUsingLaunch 끝
        // 3 - buildByUsingLaunch - coroutineScope 영역 시작
        // 4 - buildByUsingLaunch - coroutineScope 영역 끝
    }


    /**
     * 참고 자료 : https://velog.io/@jaewonkim1468/Coroutine%EC%9D%98-Job
     *
     * 1-1 job이란??
     * Job은 Kotlin Coroutine을 컨트롤 하기 위한 것 입니다.
     * Job을 통해서 하나 혹은 여러 개의 coroutine을 제어할 수 있습니다.
     *
     * Job의 메소드
     *
     * a) start : job을 실행합니다. start()의 반환값은 Boolean 입니다.
     * 호출했을 때 코루틴이 동작중이면 true, 준비 또는 완료상태면 false를 반환합니다.
     *
     * start로 생성된 job은 중단(suspend) 없이 실행됩니다. 따라서 CoroutineScope안에서 실행되지 않아도 됩니다.
     * 대신 start를 호출한 스레드가 종료되면 job도 같이 종료됩니다.
     *
     * b) join : job을 실행합니다. 대신 start와 다르게 Job의 동작이 완료될 때까지 Join을 호출한 코루틴을 일시 중단 합니다.
     *
     * MainThread가 job이 끝날 때 까지 일시중단되기 때문에 "print!!"가 출력됩니다.
     */
    private fun jobStart() {
        Log.d("MyTag", "checkJob - start")
        val activeJob: Job = coroutineScope.launch {
            Log.d("MyTag", "activeJob - start")
            delay(1000)
            Log.d("MyTag", "activeJob - end")
        }
        Log.d("MyTag", "$activeJob")
        activeJob.start()    // 1초 대기
        Log.d("MyTag", "$activeJob")
        Log.d("MyTag", "checkJob - end")
    }

    private suspend fun jobJoin() {
        Log.d("MyTag", "checkJob - start")
        val activeJob: Job = coroutineScope.launch {
            Log.d("MyTag", "activeJob - start")
            delay(1000)
            Log.d("MyTag", "activeJob - end")
        }
        Log.d("MyTag", "$activeJob")
        activeJob.join()    // 1초 대기
        Log.d("MyTag", "$activeJob")
        Log.d("MyTag", "checkJob - end")
    }


}