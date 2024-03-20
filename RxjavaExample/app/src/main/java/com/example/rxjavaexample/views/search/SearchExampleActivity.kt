package com.example.rxjavaexample.views.search

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxjavaexample.R
import com.example.rxjavaexample.dataModel.SampleModel
import com.example.rxjavaexample.views.adapter.SearchExampleAdapter
import com.jakewharton.rxbinding4.widget.textChanges
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class SearchExampleActivity : AppCompatActivity() {

    private val TAG = "sample example test"

    private lateinit var editText: EditText

    private lateinit var searchViewModel: SearchExampleViewModel

    private lateinit var listview: RecyclerView

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_example)

        editText = findViewById(R.id.editText)
        listview = findViewById(R.id.listview)

        searchViewModel = ViewModelProvider(this)[SearchExampleViewModel::class.java]
        searchViewModel.addSample()
        searchViewModel.checkDataSet()


        val searchAdapter = SearchExampleAdapter(searchViewModel.sampleList)
        listview.adapter = searchAdapter
        listview.layoutManager = LinearLayoutManager(this)



        editText.textChanges()
            .debounce(100, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { o ->
                Log.d(TAG, o.toString())
                val list = mutableListOf<SampleModel>()
                for (sampleValue in searchViewModel.sampleList) {
                    if (sampleValue.title.contains(o)) {
                        list.add(sampleValue)
                    }
                }

                searchAdapter.setDataSet(list)
                searchAdapter.dataChanged()
            }
    }
}
