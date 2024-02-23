package com.example.rxjavapractice

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.observers.DisposableObserver

/**
 * 참고 url
 * 1. : https://hjlab.tistory.com/418 (안드로이드에서 Rxjava 사용하는 내용들 나와있음)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textView = findViewById(R.id.textView)

        // Todo: 여기서 붙어 있는 object가 무엇인지 알아보기
        val observer = object : DisposableObserver<String>() {
            override fun onNext(s: String) {
                textView.text = s
            }

            override fun onError(e: Throwable) {
            }

            override fun onComplete() {
            }
        }

        Observable.create { emitter ->
            emitter.onNext("hello")
            emitter.onComplete()
        }.subscribe(observer)


        Observable.create<String> { emitter ->
            emitter.onNext("Hello world1")
            emitter.onNext("Hello world2")
            emitter.onComplete()
        }.subscribe { o ->
            textView.text = o
        }



    }
}