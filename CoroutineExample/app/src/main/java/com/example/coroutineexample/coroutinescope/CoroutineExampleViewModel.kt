package com.example.coroutineexample.coroutinescope

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineExampleViewModel : ViewModel() {


    private val _userDataLiveData: MutableLiveData<UserData> = MutableLiveData()
    val userDataLiveData: LiveData<UserData> = _userDataLiveData

    fun fetchData() {
        viewModelScope.launch {
            // 네트워크 요청 등의 비동기 작업을 수행합니다.
            val result = fetchUserData()

            // LiveData를 통해 UI에 데이터를 업데이트합니다.
            _userDataLiveData.postValue(result)
        }
    }

    private suspend fun fetchUserData(): UserData {
        // 네트워크 요청 등의 작업을 수행합니다.
        delay(1000) // 예시로 1초 지연
        return UserData("John Doe", 30) // 예시 데이터
    }
}

data class UserData(val name: String, val age: Int)
