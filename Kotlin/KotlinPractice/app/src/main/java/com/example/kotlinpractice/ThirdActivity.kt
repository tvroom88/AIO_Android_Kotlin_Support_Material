package com.example.kotlinpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
    }
    
    // 1.변수 - 생략

    /** 
     * 2,형 변환
     * 코틀린에서는 to변수()를 통해 형변환 가능
     * 코틀린은 암시적 형변환을 지원하지 않음
     */
    
    var a = 123
    var b = a.toString()
    
    // 3.배열
    var intArr:Array<Int> = arrayOf(1, 2, 3, 4)
    var intArr2 = arrayOfNulls<Int>(5)
    
    //Any는 데이터 타입의 최상위(어느 데이터든 다 들어갈 수 있음)
    var anyArr: Array<Any> = arrayOf(1, "awd", 3.2, 4)

    
    //4, 함수
    fun main(){
        print(add(1,2,3))
    }
    
    // 단일 표현식 함수
    fun add(a:Int, b:Int, c:Int) = a+b+c

}