package com.example.hiltapplication.store

import android.content.Context
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class Store @Inject constructor()  {
    val TAG = "STORE"

    fun open(){
        Log.d(TAG, "OPEN")
    }

    fun close(){
        Log.d(TAG, "CLOSE")
    }
}