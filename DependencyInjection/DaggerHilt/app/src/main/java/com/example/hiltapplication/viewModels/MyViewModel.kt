package com.example.hiltapplication.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyViewModel @Inject constructor() : ViewModel(){

    fun good(){
        Log.d("gogogo", "gogogo");
    }
}