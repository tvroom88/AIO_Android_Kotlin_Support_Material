package com.example.rxjavaexample.views.search

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.rxjavaexample.dataModel.SampleModel

class SearchExampleViewModel : ViewModel() {

    var sampleList = mutableListOf<SampleModel>()

    fun addSample() {
        val tempList = mutableListOf<SampleModel>()
        for (num in 0..100) {
            tempList.add(SampleModel("$num ${'a' + num}"))
        }
        sampleList = tempList
    }

    fun checkDataSet(){
        for(data in sampleList){
            Log.d("SearchExampleViewModel", data.title)
        }
    }
}