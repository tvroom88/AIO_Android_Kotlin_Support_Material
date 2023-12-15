package com.example.mvp

import android.os.Handler
import android.os.Looper
import java.util.Arrays
import java.util.Random

class Model : Contract.Model {
    // array list of strings from which random strings will be selected to display in the activity
    private val arrayList =
        listOf(
            "박지성", "손흥민", "차범근", "메시", "호날두"
        )

    // this method will invoke when user clicks on the button and it will take a delay of 300 milliseconds to display next course detail
    override fun getNextPlayer(onFinishedListener: Contract.Model.OnFinishedListener?) {
        Handler(Looper.getMainLooper()).postDelayed({ onFinishedListener!!.onFinished(getRandomString) }, 300)
    }


    // method to select random string from the list of strings
    private val getRandomString: String
        get() {
            val random = Random()
            val index = random.nextInt(arrayList.size)
            return arrayList[index]
        }
}