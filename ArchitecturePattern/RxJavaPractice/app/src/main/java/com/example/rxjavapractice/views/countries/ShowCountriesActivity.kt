package com.example.rxjavapractice.views.countries

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.rxjavapractice.dataModel.CountryModel
import com.example.rxjavapractice.views.adapter.CountryListAdapter


class ShowCountriesActivity : AppCompatActivity() {
    var countriesList: RecyclerView? = null
    var listError: TextView? = null
    var loadingView: ProgressBar? = null
    var refreshLayout: SwipeRefreshLayout? = null


    private val viewModel: ShowCountriesViewModel by viewModels()
    private val adapter: CountryListAdapter = CountryListAdapter(ArrayList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.rxjavapractice.R.layout.activity_second)
    }
}
