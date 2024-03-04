package com.example.rxjavapractice

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.rxjavapractice.views.countries.ShowCountriesActivity
import io.reactivex.Observable
import io.reactivex.observers.DisposableObserver


/**
 * 참고 url
 * 1. : https://hjlab.tistory.com/418 (안드로이드에서 Rxjava 사용하는 내용들 나와있음)
 */
class MainActivity : AppCompatActivity() {

    private lateinit var textView: TextView
    private lateinit var exBtn1: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        textView = findViewById(R.id.textView)
        exBtn1 = findViewById(R.id.exBtn1)

        exBtn1.setOnClickListener {
            val intent = Intent(this, ShowCountriesActivity::class.java)
            startActivity(intent)
        }

        simpleObservableUsage()
        observableClassFactoryFunction()
    }

    private fun simpleObservableUsage() {
        // 1
        val observer = object : DisposableObserver<String>() {
            override fun onNext(s: String) {
                textView.text = s
            }

            override fun onError(e: Throwable) {}
            override fun onComplete() {}
        }

        Observable.create { emitter ->
            emitter.onNext("hello")
            emitter.onComplete()
        }.subscribe(observer)

        // 2
        Observable.create<String> { emitter ->
            emitter.onNext("Hello world1")
            emitter.onNext("Hello world2")
            emitter.onComplete()
        }.subscribe { o ->
            textView.text = o
        }

        // 3
        Observable.just("changed")
            .subscribe { o ->
                textView.text = o
            }

        // 4
        Observable.just(1, 2, 3)
            .map { x: Int -> x * 10 }
            .subscribe { x: Int? -> println(x) }
    }

    private fun observableClassFactoryFunction() {
        Observable.just("a", "b", "c", "d", "e")
            .subscribe(System.out::println)
    }
}
