package com.example.hiltapplication.animal

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
//private val mContext: Context
class Dog @Inject constructor(@ActivityContext private var mContext:Context) : Animal {
    val TAG = "Animal - Dog"

    override fun bark() {
        Log.d(TAG, "멍멍")
        Toast.makeText(mContext, "멍멍", Toast.LENGTH_LONG).show()
    }

    override fun speed() {
        Log.d(TAG, "15km/h")
    }

    override fun size() {
        Log.d(TAG, "small")

    }

}