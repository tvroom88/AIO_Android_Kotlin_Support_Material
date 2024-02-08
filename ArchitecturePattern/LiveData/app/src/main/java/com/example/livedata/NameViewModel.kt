package com.example.livedata

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel : ViewModel() {

    // String 타입의 LiveData 생성

    /**
     * MutableLiveData는 mutable하고, LiveData는 immutable하기 떄문이다.
     * 즉, LiveData는 immutable하기에 thread-safe 하지만 값을 변경할 수 없어서 MutableLiveData를 통해 값을 변경하고 LiveData는 해당 값을 immutable하게 받는 것이다.
     */
    private val _currentName: MutableLiveData<String> = MutableLiveData("천재")
    val currentName: LiveData<String> = _currentName


    fun changeName(name: String){
        if(name == ""){
            return
        }
        _currentName.value = name
    }
}