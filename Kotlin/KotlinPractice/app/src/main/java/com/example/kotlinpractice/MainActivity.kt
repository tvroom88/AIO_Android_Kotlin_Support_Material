package com.example.kotlinpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val kotlinBasicA = KotlinBasicA()
        kotlinBasicA.test()

        /**
         * 1. KotlinBasicA : 클래스와 프로퍼티
         */

        // 1. 변수 선언
        val immutable_string = ""
        var count: Int = 10
        count = 15

        // 2. 타입 추론
        val languageName = "Kotlin"
        val upperCaseName = languageName.uppercase()

        // 3. Null 안전
        val langName: String? = null
        println(langName?.uppercase())

        // 4. 조건부
        if (count == 42) {
            println("I have the answer.")
        } else {
            println("The answer eludes me.")
        }

        // 유형 선언을 생략할 수 있지만 명확히 하기 위해 유형 선언을 포함하는 것이 좋습니다.
        val answerString: String = if (count == 42) {
            "I have answer."
        } else {
            "I don't have answer."
        }

        val newAnswerString = when {
            count == 42 -> "I have the answer."
            count < 22 -> "The answer is closed."
            else -> "I don't have the answer."
        }


        /**
         * 5. 함수 : 하나 이상의 표현식을 함수로 그룹화할 수 있습니다.
         * 결과가 필요할 때마다 동일한 일련의 표현식을 반복하는 대신 함수에 표현식을 포함한 다음 함수를 호출할 수 있습니다.
         */

        fun generateAnswerString(count: Int): String {
            val answerString = when (count) {
                42 -> "I have the answer"
                else -> "The answer eludes me"
            }
            return answerString
        }

        // 함수 선언의 단순화 - 로컬 변수 선언을 건너뛰고 바로 결과를 return 할 수도 있다.
        fun generateAnswerString2(count: Int): String {
            return if (count > 3) {
                "I have the anser"
            } else {
                "The answer eludes me"
            }
        }

        //return 키워드를 할당 연산자로 바꿀 수도 있습니다.
        fun generateAnswerString3(count: Int): String = if (count > 3) {
            "I have the anser"
        } else {
            "The answer eludes me"
        }


        /**
         *  6. 익명 함수
         *  모든 함수에 이름이 필요하지는 않습니다. 일부 함수는 입력과 출력에 의해 더 직접적으로 식별됩니다. 이러한 함수를 익명 함수라고 합니다.
         *  이 참조를 사용하여 나중에 익명 함수를 호출하면 익명 함수 참조를 유지할 수 있습니다.
         */

        val stringLengthFunc: (String) -> Int = { input ->
            input.length
        }

        val stringLength = stringLengthFunc("Android")
        print(stringLength)

        val test1: (String) -> Int = {
            val a = 3
            val b = 3
            a + b
        }

        /**
         * 7. 고차 함수
         * 함수는 다른 함수를 인수로 취할 수 있습니다. 다른 함수를 인수로 사용하는 함수를 고차 함수라고 합니다.
         * 이 패턴은 자바에서 콜백 인터페이스를 사용할 때와 동일한 방식으로 구성요소 간에 통신하는 데 유용합니다.
         */

        fun stringMapper(str: String, mapper: (String) -> Int): Int {
            return mapper(str)
        }

        val test2: Int = stringMapper("Android") { input ->
            val num = 3
            num + input.length
        }

        val car = Car()
        val wheels = car.wheels

    }

    /**
     * 8. 클래스 :
     * 위에 언급된 것들은 모두 코틀린 프로그래밍 언어 안에 내장 되어 있습니다.
     * 만약 너가 커스텀 타입을 추가하려면 class 키워드를 사용해서 추가!
     */

    class Wheel {}

    class Car {
        val wheels = listOf<Wheel>()
    }

    class Car2(val wheels: List<Wheel>)

    /**
     * 9. 클래스 함수 및 캡슐화
     * 클래스는 함수를 사용하여 동작을 모델링합니다. 함수는 상태를 수정할 수 있으므로 노출하려는 데이터만 노출할 수 있습니다.
     */

    class DoorLock {}
    class Key {}
    class Car3(val wheels: List<Wheel>) {
        private val doorLock: DoorLock = DoorLock()

        var gallonsOfFuelInTank: Int = 15
            private set

        fun unlockDoor(key: Key): Boolean {
            // Return true if key is valid for door lock, false otherwise
            return true
        }
    }
}