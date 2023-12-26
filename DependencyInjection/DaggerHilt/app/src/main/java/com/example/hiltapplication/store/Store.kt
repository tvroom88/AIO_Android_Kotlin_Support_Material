package com.example.hiltapplication.store

import android.util.Log
import android.widget.Toast
import javax.inject.Inject

class Store @Inject constructor()  {
    val TAG = "STORE"

    fun open(){
        Log.d(TAG, "OPEN")
//        Toast.makeText(this, "Store is opened", Toast.LENGTH_SHORT).show()
    }

    fun close(){
        Log.d(TAG, "CLOSE")
    }
}