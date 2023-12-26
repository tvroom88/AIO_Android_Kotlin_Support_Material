package com.example.hiltapplication.animal

import android.util.Log
import javax.inject.Inject

class Cat @Inject constructor() : Animal {
    val TAG = "Animal - Cat"

    override fun bark() {
        Log.d(TAG, "나옹냐옹")
    }

    override fun speed() {
        Log.d(TAG, "10km/h")
    }

    override fun size() {
        Log.d(TAG, "small")

    }
}