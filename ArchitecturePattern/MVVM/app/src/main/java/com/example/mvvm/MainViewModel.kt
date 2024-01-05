package com.example.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private var model: Model = Model()
    private var _character = MutableLiveData<MyCharacter>()

    val character: LiveData<MyCharacter>
        get() = _character

    fun getRandomCharacter() {
        _character.value = model.getRandomCharacter()
    }

    fun isShow(str: String?): Boolean {
        return str != null && str != "0"
    }
}