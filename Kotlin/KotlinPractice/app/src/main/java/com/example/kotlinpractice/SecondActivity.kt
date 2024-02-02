package com.example.kotlinpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.IllegalArgumentException

enum class Color(val r: Int, val g: Int, val b: Int) {
    RED(255, 0, 0), ORANGE(255, 165, 0),
    YELLOW(255, 255, 0), GREEN(0, 255, 0);

    fun rgb() = (r * 256 + g) * 256 + b
}

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val person = Person("good", 29)
        println(person.name)
        println(person.age)


    }

    fun eval(e: Expr): Int =
        if (e is Num) {
            e.value
        } else if (e is Sum) {
            eval(e.left) + eval(e.right)
        } else {
            throw IllegalArgumentException("Unknown expression")
        }

    fun eval2(e: Expr): Int =
        when (e) {
            is Num -> {
                e.value
            }

            is Sum -> {
                eval(e.left) + eval(e.right)
            }

            else -> {
                throw IllegalArgumentException("Unknown expression")
            }
        }

    fun forEach() {
        val list = listOf("a", "b", "c")
    }

    // --- with 함수 ---
    fun alphabet(): String {
        val result = StringBuilder()
        for (letter in 'A'..'Z') {
            result.append(letter)
        }
        result.append("\nNow I Know the alphabet!")
        return result.toString()
    }

    fun alphabet2() = with(StringBuilder()) {
        for (letter in 'A'..'Z') {
            append(letter)
        }
    }

    fun test(s: String) = with(s) {
        val len = length
    }

    // --- apply ---
    fun alphabetUsingApply() = StringBuilder().apply {
        for(letter in 'A'..'Z'){
            append(letter)
            append(" ")
        }
    }
}

class Person(val name: String, val age: Int) {
    val isProgrammer: Boolean
        get() {
            return name == "levi"
        }
}

interface Expr
class Num(val value: Int) : Expr
class Sum(val left: Expr, val right: Expr) : Expr

