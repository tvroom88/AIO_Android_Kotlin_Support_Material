package com.example.kotlinpractice

/**
 * KotlinBasicA : 클래스와 프로퍼티
 */
class KotlinBasicA {

    var rectangle1 = Rectangle1()


    fun test(){
//        rectangle1.age = 3
        println("herehere ${rectangle1.age}")
    }

}

class Person(val name: String, var isMarried: Boolean)

/**
 * class
 * - 코틀린의 경우 getter/setter 메소드를 기본적으로 제공한다.
 *
 * property
 * - val name 구문: 읽기 전용 프로퍼티로, 코틀린은 private 필드(default)와 필드를 읽는 public 게터를 만들어낸다.
 * - var isMarried 구문: 읽고 쓸 수 있는 프로퍼티로, 코틀린은 private 필드와 public 게터, public 세터를 만들어낸다.
 * - 필드의 게터/세터뿐만 아니라 기본적으로 생성자가 필드를 초기화하는 구현이 숨겨져있다.
 */


class Rectangle(val height:Int, val width:Int) {
    val isSquare: Boolean
        get() {
            return height == width
        }
}

/**
 * isSqure 프로퍼티는 자체 값을 저장하는 필드가 필요 없다. 이 프로퍼티에는 자체 구현을 제공하는 getter만 존재한다.
 *
 */

class Rectangle1 {
    var age = 32
        get() {
            println("access age info")
            return field
        }
        set(value) {
            if(value != null)
                println("value is not null")
            field = value
        }
}


