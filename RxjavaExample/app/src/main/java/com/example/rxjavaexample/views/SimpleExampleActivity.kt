package com.example.rxjavaexample.views

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavaexample.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.observers.DisposableObserver

class SimpleExampleActivity : AppCompatActivity() {

    private var testTextView1: TextView? = null
    private var testTextView2: TextView? = null
    private var testTextView3: TextView? = null
    private var testTextView4: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_simple_example)

        testTextView1 = findViewById(R.id.testTextView1)
        testTextView2 = findViewById(R.id.testTextView2)
        testTextView3 = findViewById(R.id.testTextView3)
        testTextView4 = findViewById(R.id.testTextView4)

        example1()

        example2()

        example3()

        example4()
    }

    // 1. Observer과 Observable을 생성한 예
    private fun example1(){
        val observer = object : DisposableObserver<String>() {
            override fun onNext(s: String) {
                testTextView1?.text = s
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        }

        Observable.create { emitter ->
            emitter.onNext("hello")
            emitter.onComplete()
        }.subscribe(observer)
    }


    // 2. Observer 생성 없이
    @SuppressLint("CheckResult")
    private fun example2(){
        Observable.create<String> { emitter ->
            emitter.onNext("Hello world")
            emitter.onComplete()
        }.subscribe { o ->
            testTextView2?.text = o
        }
    }


    // 3. Observable just 활용
    @SuppressLint("CheckResult")
    private fun example3(){
        Observable.just("changed")
            .subscribe { o ->
                testTextView3?.text = o
            }
    }

    private fun example4(){
        val source: Observable<String> = Observable.just("Hello Single")
        Single.fromObservable(source)
            .subscribe { o ->
                testTextView4?.text = o
            }
            .dispose()
    }
}