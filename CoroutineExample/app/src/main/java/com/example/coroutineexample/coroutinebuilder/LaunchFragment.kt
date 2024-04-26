package com.example.coroutineexample.coroutinebuilder

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.coroutineexample.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
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

        buildByUsingLaunch() // 1. Coroutine launch
        jobStart() //  1-1. Job

//        CoroutineScope(Dispatchers.Main).launch {
//            jobJoin()
//              Log.d("MyTag", "jobJoin in onCreateView - end")
//        }


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

        // 상태 확인 방법 :
        job.isActive
        job.isCancelled
        job.isCompleted
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
     * start로 생성된 job은 중단(suspend) 없이 실행됩니다. 따라서 CoroutineScope안에서 실행 되지 않아도 됩니다.
     * 대신 start를 호출한 스레드가 종료되면 job도 같이 종료됩니다.
     *
     * 그냥 계속 진행됨
     */
    private fun jobStart() {
        Log.d("MyTag", "jobStart - start")

        val activeJob: Job = coroutineScope.launch(start = CoroutineStart.LAZY) {
            Log.d("MyTag", "jobStart inside - start")
            delay(2000)
            Log.d("MyTag", "jobStart inside - end")
        }

        //  1-1. Job
        Log.d("MyTag", "$activeJob")
        activeJob.start()    // 1초 대기
        Log.d("MyTag", "$activeJob")
        Log.d("MyTag", "jobStart - end")

        // jobStart - start
        // StandaloneCoroutine{Active}@fd97f26
        // StandaloneCoroutine{Active}@fd97f26
        // jobStart - end
        // jobStart inside - start
        // jobStart inside - end
    }

    /**
     * b) join : job을 실행합니다. 대신 start와 다르게 Job의 동작이 완료될 때까지 Join을 호출한 코루틴을 일시 중단 합니다.
     *
     * 밑에 결과를 보니 job이 join된다면 끝날때까지 다른건 job이 끝날때까지 stop 되는 듯
     */

    private suspend fun jobJoin() {
        Log.d("MyTag", "jobJoin - start")
        val activeJob: Job = coroutineScope.launch {
            Log.d("MyTag", "jobJoin - start")
            delay(3000)
            Log.d("MyTag", "jobJoin - end")
        }
        Log.d("MyTag", "$activeJob")
        activeJob.join()    // 1초 대기
        Log.d("MyTag", "$activeJob")
        delay(3000)
        Log.d("MyTag", "jobJoin - end")

        // jobJoin - start
        // StandaloneCoroutine{Active}@cc6cfe5
        // jobJoin - start
        // jobJoin - end
        // StandaloneCoroutine{Completed}@cc6cfe5
        // jobJoin - end
    }

    /**
     * c) cancel(): 현재 코루틴에 종료를 유도하고 start()와 마찬가지로 대기하지 않습니다.
     * 만약 타이트하게 동작하는 반복문에 delay가 없다면 종료하지 못합니다.
     *
     * d) cancelAndJoin(): 현재 코루틴을 즉시 종료하라는 신호를 보낸 후 정상 종료될 때까지 대기합니다.
     *
     * e) cancelChildre() : CoroutineScope 내에 작성한 자식 코루틴들을 종료합니다.
     * cancel과 다르게 하위 아이템들만 종료하며, 상위 코루틴은 취소하지 않습니다.
     */
}